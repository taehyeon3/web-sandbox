import React, {useCallback, useMemo, useRef, useState} from "react";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";

function PostCreate() {
    const [title, setTitle] = useState("");
    const [content, setContent] = useState(""); // HTML로 저장
    const [imageUrls, setImageUrls] = useState([]);
    const quillRef = useRef();

    // 이미지 업로드 핸들러 (useCallback으로 감싸기)
    const imageHandler = useCallback(() => {
        const input = document.createElement("input");
        input.setAttribute("type", "file");
        input.setAttribute("accept", "image/*");
        input.click();
        input.onchange = async () => {
            const file = input.files[0];
            if (!file) return;
            const formData = new FormData();
            formData.append("image", file);
            // 서버에 이미지 업로드
            const res = await fetch("/api/images", {method: "POST", body: formData});
            if (!res.ok) {
                alert("이미지 업로드 실패");
                return;
            }
            const data = await res.json();
            const url = data.url;
            // 에디터에 이미지 삽입
            const editor = quillRef.current.getEditor();
            const range = editor.getSelection();
            editor.insertEmbed(range ? range.index : 0, "image", url);
            setImageUrls(prev => [...prev, url]);
        };
    }, []);

    // 이미지 삭제 감지
    const handleChange = useCallback((html) => {
        setContent(html);
        // 현재 에디터의 이미지 src 추출
        const div = document.createElement("div");
        div.innerHTML = html;
        const imgs = Array.from(div.querySelectorAll("img")).map(img => img.src);
        // 삭제된 이미지 탐지
        const deleted = imageUrls.filter(url => !imgs.includes(url));
        deleted.forEach(async (url) => {
            await fetch(`/api/images?url=${encodeURIComponent(url)}`, {method: "DELETE"});
        });
        setImageUrls(imgs);
    }, [imageUrls]);

    // modules 객체 useMemo로 감싸기
    const modules = useMemo(() => ({
        toolbar: {
            container: [
                [{header: [1, 2, false]}],
                ["bold", "italic", "underline", "strike"],
                ["link", "image"],
                [{list: "ordered"}, {list: "bullet"}],
                ["clean"],
            ],
            handlers: {image: imageHandler},
        },
    }), [imageHandler]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!title.trim() || !content.trim()) {
            alert("제목과 내용을 모두 입력하세요.");
            return;
        }
        await fetch("/posts", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({title, content}),
        });
        // 성공 시 처리 (예: 목록 이동)
        alert("등록 완료!");
    };

    return (
        <form onSubmit={handleSubmit} style={{maxWidth: 700, margin: "0 auto"}}>
            <h2>게시글 작성</h2>
            <input
                type="text"
                value={title}
                required
                onChange={e => setTitle(e.target.value)}
                placeholder="제목"
                style={{width: "100%", marginBottom: 12, fontSize: 18, padding: 8}}
            />
            <ReactQuill
                ref={quillRef}
                value={content}
                onChange={handleChange}
                modules={modules}
                theme="snow"
                placeholder="내용을 입력하세요. 이미지는 드래그/붙여넣기/툴바로 추가할 수 있습니다."
                style={{height: 350, marginBottom: 24}}
            />
            <button type="submit" style={{width: "100%", padding: 12, fontWeight: "bold"}}>등록</button>
        </form>
    );
}

export default PostCreate;
