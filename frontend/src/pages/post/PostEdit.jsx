// PostEdit.jsx
import React, {useEffect, useState} from 'react';
import {useParams} from 'react-router-dom';
import PostForm from './PostForm';
import api from '../../api/axiosInstance';

const PostEdit = () => {
    const {id} = useParams();
    const [post, setPost] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchPost = async () => {
            try {
                const response = await api.get(`/posts/${id}`);
                setPost(response.data);
            } catch (err) {
                setError('게시글을 불러오는데 실패했습니다.');
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        fetchPost();
    }, [id]);

    if (loading) return <div className="loading-container">게시글을 불러오는 중...</div>;
    if (error) return <div className="error-container">{error}</div>;
    if (!post) return <div className="error-container">게시글을 찾을 수 없습니다.</div>;

    return <PostForm mode="edit" initialData={post}/>;
};

export default PostEdit;
