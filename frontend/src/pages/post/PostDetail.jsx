import React, {useEffect, useState} from 'react';
import {Link, useParams} from 'react-router-dom';
import LogoLink from "../../components/LogoLink.jsx";
import api from "../../api/axiosInstance.jsx"

const PostDetail = () => {
    const {id} = useParams();
    const [post, setPost] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        api.get(`/posts/${id}`)
            .then(res => {
                if (res.status !== 200)
                    throw new Error('게시글을 불러올 수 없습니다.');
                return res.data;
            })
            .then(data => setPost(data))
            .catch(err => setError(err.message))
            .finally(() => setLoading(false));
    }, [id]);

    if (loading) return <div>로딩 중...</div>;
    if (error) return <div className="alert alert-danger">{error}</div>;
    if (!post) return <div>게시글이 존재하지 않습니다.</div>;

    return (
        <div className="potato-post-detail-container">
            <div className="text-center mb-4">
                <LogoLink width="120"/>
                <h2 className="potato-title">{post.title}</h2>
            </div>
            <div className="mb-3">
                <div className="text-muted small mb-2">
                    작성자: {post.author} | 좋아요: {post.likeCount} | 조회수: {post.viewCount}
                </div>
                {/* content가 HTML일 경우 안전하게 렌더링 */}
                <div
                    className="potato-post-content"
                    dangerouslySetInnerHTML={{__html: post.content}}
                />
            </div>
            <div className="mt-4 text-end">
                <Link to="/posts" className="btn btn-outline-secondary">목록으로</Link>
            </div>
        </div>
    );
};

export default PostDetail;
