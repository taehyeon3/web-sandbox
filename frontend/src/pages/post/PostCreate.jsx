import React, {useEffect, useMemo, useRef, useState} from 'react';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import '../../style/PostCreate.css';
import api from '../../api/axiosInstance.jsx';
import {useNavigate} from 'react-router-dom';

const PostCreate = () => {
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const quillRef = useRef(null);
    const navigate = useNavigate();
    const [uploadedImages, setUploadedImages] = useState([]);

    // 이미지 업로드 함수 (재사용을 위해 분리)
    const uploadImage = async (file) => {
        setLoading(true);
        try {
            const formData = new FormData();
            formData.append('image', file);

            const response = await api.post('/post-images', formData, {
                headers: {'Content-Type': 'multipart/form-data'}
            });

            const fileUrl = response.data.fileUrl;
            const imageId = response.data.id;
            console.log('업로드 성공 : ' + imageId);
            console.log('url' + fileUrl);

            // 에디터에 이미지 삽입
            const quill = quillRef.current.getEditor();
            const range = quill.getSelection() || {index: quill.getLength()};
            quill.insertEmbed(range.index, 'image', fileUrl);


            setUploadedImages(prev => [...prev, {id: imageId, url: fileUrl}]);
            //data-id 속성 추가
            setTimeout(() => {
                const editor = quillRef.current.getEditor();
                const imgs = editor.root.querySelectorAll(`img[src="${fileUrl}"]`);
                imgs.forEach(img => img.setAttribute('data-id', imageId));
            }, 0);

            // 커서 위치 조정
            quill.setSelection((range.index) + 1);
            return true;
        } catch (err) {
            console.error('이미지 업로드 실패:', err);
            setError('이미지 업로드에 실패했습니다.');
            setTimeout(() => setError(''), 3000);
            return false;
        } finally {
            setLoading(false);
        }
    };

    // 이미지 업로드 핸들러 (툴바 버튼용)
    function imageHandler() {
        const input = document.createElement('input');
        input.setAttribute('type', 'file');
        input.setAttribute('accept', 'image/*');
        input.click();

        input.onchange = async () => {
            if (input.files && input.files[0]) {
                await uploadImage(input.files[0]);
            }
        };
    }

    // 드래그 앤 드롭 이벤트 핸들러 설정
    useEffect(() => {
        if (!quillRef.current) return;

        const editor = quillRef.current.getEditor();
        const editorRoot = editor.root;

        const handleDragOver = (e) => {
            e.preventDefault();
            e.stopPropagation();
        };

        const handleDrop = async (e) => {
            e.preventDefault();
            e.stopPropagation();

            if (e.dataTransfer.files && e.dataTransfer.files.length > 0) {
                const file = e.dataTransfer.files[0];
                if (file.type.startsWith('image/')) {
                    await uploadImage(file);
                }
            }
        };

        editorRoot.addEventListener('dragover', handleDragOver);
        editorRoot.addEventListener('drop', handleDrop);

        return () => {
            editorRoot.removeEventListener('dragover', handleDragOver);
            editorRoot.removeEventListener('drop', handleDrop);
        };
    }, [quillRef.current]);

    // useMemo로 modules 객체를 메모이제이션 (버그 방지)
    const modules = useMemo(() => ({
        toolbar: {
            container: [
                [{'header': [1, 2, 3, false]}],
                ['bold', 'italic', 'underline', 'strike', 'blockquote'],
                [{'list': 'ordered'}, {'list': 'bullet'}, {'indent': '-1'}, {'indent': '+1'}],
                ['link', 'image'],
                ['clean']
            ],
            handlers: {
                image: imageHandler
            }
        },
        clipboard: {
            matchVisual: false
        }
    }), []);

    const formats = [
        'header',
        'bold', 'italic', 'underline', 'strike', 'blockquote',
        'list', 'bullet', 'indent',
        'link', 'image'
    ];

    // 게시글 등록 핸들러
    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!title.trim() || !content.trim()) {
            alert('제목과 내용을 모두 입력해주세요!');
            return;
        }

        setLoading(true);
        setError('');

        try {
            // 게시글 데이터 준비
            const postData = {
                title,
                content
            };

            // 서버에 게시글 등록 요청
            await api.post('/posts', postData);

            // 성공 시 게시글 목록 페이지로 이동
            alert('게시글이 성공적으로 등록되었습니다.');
            navigate('/posts');
        } catch (err) {
            console.error('게시글 등록 실패:', err);
            setError('게시글 등록에 실패했습니다. 다시 시도해주세요.');
        } finally {
            setLoading(false);
        }
    };

    // 이미지 삭제 함수 (에디터에서 이미지가 삭제될 때 서버에서도 삭제)
    const handleContentChange = (value) => {
        // 현재 에디터 내용에서 이미지 URL과 ID 추출
        const currentImages = extractImagesFromHTML(value);

        // 삭제된 이미지 찾기
        const deletedImages = uploadedImages.filter(
            uploadedImg => !currentImages.some(curImg => curImg.url === uploadedImg.url)
        );

        // 삭제된 이미지가 있으면 서버에 삭제 요청
        deletedImages.forEach(async (img) => {
            if (img.id) {
                try {
                    console.log('삭제' + img.id);
                    await api.delete(`/post-images/${img.id}`);
                    setUploadedImages(prev => prev.filter(image => image.id !== img.id));
                } catch (err) {
                    console.error('이미지 삭제 실패:', err);
                }
            }
        });

        // 에디터 내용 업데이트
        setContent(value);
    };

    // HTML에서 이미지 URL과 ID 추출하는 함수
    const extractImagesFromHTML = (html) => {
        const div = document.createElement('div');
        div.innerHTML = html;
        const imgs = Array.from(div.querySelectorAll('img'));
        return imgs.map(img => ({
            url: img.getAttribute('src'),
            id: img.getAttribute('data-id')
        }));
    };

    return (
        <div className="potato-post-create-container">
            <h2 className="potato-title">게시글 작성</h2>
            {error && <div className="potato-error">{error}</div>}
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label className="potato-label" htmlFor="title">제목</label>
                    <input
                        id="title"
                        className="potato-input"
                        type="text"
                        value={title}
                        onChange={e => setTitle(e.target.value)}
                        placeholder="제목을 입력하세요"
                        maxLength={100}
                        required
                        disabled={loading}
                    />
                </div>
                <div className="form-group">
                    <label className="potato-label">내용 (이미지/HTML 가능)</label>
                    <div className="quill-container">
                        <ReactQuill
                            ref={quillRef}
                            theme="snow"
                            value={content}
                            onChange={handleContentChange}
                            modules={modules}
                            formats={formats}
                            placeholder="감자에 대한 이야기를 자유롭게 써보세요! 이미지는 드래그해서 놓으셔도 됩니다."
                            className="potato-quill-editor"
                            readOnly={loading}
                        />
                        {loading && <div className="quill-loading-overlay">이미지 업로드 중...</div>}
                    </div>
                </div>
                <div className="button-group">
                    <button
                        type="button"
                        className="potato-cancel-btn"
                        onClick={() => {
                            uploadedImages.map(img =>
                                api.delete(`/post-images/${img.id}`)
                            );
                            navigate('/posts');
                        }}
                        disabled={loading}
                    >
                        취소
                    </button>
                    <button
                        className="potato-btn"
                        type="submit"
                        disabled={loading}
                    >
                        {loading ? '처리 중...' : '등록'}
                    </button>
                </div>
            </form>
        </div>
    );
};

export default PostCreate;
