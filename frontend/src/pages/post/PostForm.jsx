// PostForm.jsx - 공통 컴포넌트
import React, {useEffect, useMemo, useRef, useState} from 'react';
import ReactQuill, {Quill} from 'react-quill-new';
import 'react-quill-new/dist/quill.snow.css';
import '../../style/PostCreate.css';
import api from '../../api/axiosInstance.jsx';
import {useNavigate} from 'react-router-dom';
import ImageResize from 'quill-image-resize-module-react';

const BaseImage = Quill.import('formats/image');

const ATTRIBUTES = ['alt', 'height', 'width', 'style', 'class'];

class CustomImage extends BaseImage {
    static formats(domNode) {
        return ATTRIBUTES.reduce((formats, attribute) => {
            if (domNode.hasAttribute(attribute)) {
                formats[attribute] = domNode.getAttribute(attribute);
            }
            return formats;
        }, {});
    }

    format(name, value) {
        if (ATTRIBUTES.indexOf(name) > -1) {
            if (value) {
                this.domNode.setAttribute(name, value);
            } else {
                this.domNode.removeAttribute(name);
            }
        } else {
            super.format(name, value);
        }
    }
}

Quill.register('formats/image', CustomImage, true);

if (typeof window !== 'undefined') {
    window.Quill = Quill;
    Quill.register('modules/imageResize', ImageResize);
}


const PostForm = ({initialData = null, mode = 'create'}) => {
    const [title, setTitle] = useState(initialData?.title || '');
    const [content, setContent] = useState(initialData?.content || '');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const quillRef = useRef(null);
    const navigate = useNavigate();
    const [uploadedImages, setUploadedImages] = useState([]);

    const addImagePrefix = (url) => {
        if (url.startsWith('/images/') || url.startsWith('http')) {
            return url;
        }
        if (url.startsWith('/')) {
            return `/images${url}`;
        }
        return `/images/${url}`;
    };

    useEffect(() => {
        if (mode === 'edit' && initialData?.id) {
            // 게시글 관련 이미지 정보 불러오기
            const fetchPostImages = async () => {
                try {
                    const response = await api.get(`/post-images/posts/${initialData.id}`);
                    const imageData = response.data.content;

                    const processedImageData = imageData.map(image => ({
                        ...image,
                        fileUrl: addImagePrefix(image.fileUrl)
                    }));

                    setUploadedImages(processedImageData);

                    // 에디터 로드 후 이미지에 data-id 속성 추가
                    setTimeout(() => {
                        if (quillRef.current) {
                            const editor = quillRef.current.getEditor();
                            imageData.forEach(img => {
                                if (img.fileUrl) {
                                    const imgElements = editor.root.querySelectorAll(`img[src="${img.fileUrl}"]`);
                                    imgElements.forEach(el => el.setAttribute('data-id', img.id));
                                }
                            });
                        }
                    }, 100);
                } catch (err) {
                    console.error('이미지 정보를 불러오는데 실패했습니다:', err);
                }
            };

            fetchPostImages();
        }
    }, [initialData, mode]);

    // 이미지 업로드 함수 (기존 코드와 동일)
    const uploadImage = async (file) => {
        setLoading(true);
        try {
            const formData = new FormData();
            formData.append('image', file);

            const response = await api.post('/post-images', formData, {
                headers: {'Content-Type': 'multipart/form-data'}
            });
            const fileUrl = "/images/" + response.data.fileUrl;
            const imageId = response.data.id;

            // 에디터에 이미지 삽입
            const quill = quillRef.current.getEditor();
            const range = quill.getSelection() || {index: quill.getLength()};
            quill.insertEmbed(range.index, 'image', fileUrl);

            setUploadedImages(prev => [...prev, {id: imageId, fileUrl: fileUrl}]);
            setTimeout(() => {
                const editor = quillRef.current.getEditor();
                const imgs = editor.root.querySelectorAll(`img[src="${fileUrl}"]`);
                imgs.forEach(img => img.setAttribute('data-id', imageId));
            }, 0);

            quill.setSelection((range.index) + 1);
            return true;
        } catch (err) {
            setError('이미지 업로드에 실패했습니다.');
            setTimeout(() => setError(''), 3000);
            return false;
        } finally {
            setLoading(false);
        }
    };

    // 이미지 핸들러 함수 (기존 코드와 동일)
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

    // 드래그 앤 드롭 이벤트 핸들러 (기존 코드와 동일)
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

    // modules 설정 (기존 코드와 동일)
    const modules = useMemo(() => ({
        toolbar: {
            container: [
                [{'align': [false, 'center', 'right', 'justify']}],
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
        },
        imageResize: {
            parchment: Quill.import('parchment'),
            modules: ['Resize', 'DisplaySize']
        },
    }), []);

    const formats = [
        'header',
        'bold', 'italic', 'underline', 'strike', 'blockquote',
        'list', 'indent',
        'link', 'image',
        'align'
    ];

    // 폼 제출 핸들러 (모드에 따라 다른 동작)
    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!title.trim() || !content.trim()) {
            alert('제목과 내용을 모두 입력해주세요!');
            return;
        }

        setLoading(true);
        setError('');

        try {
            const imageIds = uploadedImages.map(img => img.id);
            const postData = {title, content, imageIds};

            if (mode === 'edit' && initialData?.id) {
                // 수정 모드
                await api.put(`/posts/${initialData.id}`, postData);
                alert('게시글이 성공적으로 수정되었습니다.');
                navigate(`/posts/${initialData.id}`);
            } else {
                // 작성 모드
                await api.post('/posts', postData);
                alert('게시글이 성공적으로 등록되었습니다.');
                navigate('/posts');
            }
        } catch (err) {
            setError(`게시글 ${mode === 'edit' ? '수정' : '등록'}에 실패했습니다.`);
            console.log(err);
        } finally {
            setLoading(false);
        }
    };

    // 이미지 삭제 감지 함수 (기존 코드와 동일)
    const handleContentChange = (value) => {
        const currentImages = extractImagesFromHTML(value);
        const deletedImages = uploadedImages.filter(
            uploadedImg => !currentImages.some(curImg => curImg.url === uploadedImg.fileUrl)
        );

        deletedImages.forEach(async (img) => {
            if (img.id) {
                try {
                    await api.delete(`/post-images/${img.id}`);
                    setUploadedImages(prev => prev.filter(image => image.id !== img.id));
                } catch (err) {
                    console.error('이미지 삭제 실패:', err);
                }
            }
        });

        setContent(value);
    };

    // HTML에서 이미지 추출 함수 (기존 코드와 동일)
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
            <h2 className="potato-title">
                {mode === 'edit' ? '게시글 수정' : '게시글 작성'}
            </h2>
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
                            placeholder="글을 자유롭게 써보세요! 이미지는 드래그해서 놓으셔도 됩니다."
                            className="potato-quill-editor"
                            readOnly={loading}
                        />
                        {loading && <div className="quill-loading-overlay">이미지 업로드 중...</div>}
                    </div>
                </div>
                <div className="button-group">
                    <button
                        className="potato-btn"
                        type="submit"
                        disabled={loading}
                    >
                        {loading ? '처리 중...' : (mode === 'edit' ? '수정' : '등록')}
                    </button>
                    <button
                        type="button"
                        className="potato-cancel-btn"
                        onClick={() => {
                            // 취소 시 업로드된 이미지 삭제 (수정 모드에서는 새로 추가된 이미지만)
                            if (mode === 'create') {
                                uploadedImages.forEach(img =>
                                    api.delete(`/post-images/${img.id}`)
                                );
                            }
                            navigate(mode === 'edit' ? `/posts/${initialData.id}` : '/posts');
                        }}
                        disabled={loading}
                    >
                        취소
                    </button>
                </div>
            </form>
        </div>
    );
};

export default PostForm;
