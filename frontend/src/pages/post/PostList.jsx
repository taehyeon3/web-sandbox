// src/components/PotatoPostList.jsx
import React, {useEffect, useState} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import '../../style/PostList.css';
import api from "../../api/axiosInstance.jsx"
import potatoLogo from "../../assets/comu-car.jpeg";

const PAGE_SIZE = 10;

const PostList = () => {
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [page, setPage] = useState(0);
    const navigate = useNavigate();
    const isLoggedIn = localStorage.getItem('user');

    const [pageInfo, setPageInfo] = useState({
        first: true,
        last: false,
        number: 0,
        size: PAGE_SIZE,
        numberOfElements: 0
    });

    useEffect(() => {
        api.get(`/posts?page=${page}&size=${PAGE_SIZE}`)
            .then(res => {
                if (res.status !== 200)
                    throw new Error('게시글을 불러올 수 없습니다.');
                return res.data;
            })
            .then(data => {
                setPosts(data.content);
                setPageInfo({
                    first: data.first,
                    last: data.last,
                    number: data.number,
                    size: data.size,
                    numberOfElements: data.numberOfElements
                });
            })
            .catch(err => setError(err.message))
            .finally(() => setLoading(false));
    }, [page]);

    const handlePrevPage = () => {
        if (!pageInfo.first) setPage(page - 1);
    };

    const handleNextPage = () => {
        if (!pageInfo.last) setPage(page + 1);
    };

    const [inputPage, setInputPage] = useState('');

    // 페이지 입력창 변경 핸들러
    const handleInputChange = (e) => {
        // 숫자만 입력받기
        const value = e.target.value.replace(/[^0-9]/g, '');
        setInputPage(value);
    };

    // 페이지 이동 버튼 클릭
    const handleGoToPage = () => {
        if (inputPage === '') return;
        const pageNum = Number(inputPage) - 1; // 1페이지부터 시작하므로 -1
        if (pageNum < 0) return;
        // totalPages 정보가 있다면 범위 제한
        if (pageInfo.totalPages && pageNum >= pageInfo.totalPages) return;
        setPage(pageNum);
        setInputPage('');
    };

    // 엔터키로도 이동
    const handleInputKeyDown = (e) => {
        if (e.key === 'Enter') {
            handleGoToPage();
        }
    };


    return (
        <div className="potato-post-list-container">
            <div className="text-center mb-4">
                <img
                    src={potatoLogo}
                    alt="감자 로고"
                    className="potato-logo"
                    width={150}
                />
                <h1 className="potato-title">감자나라 커뮤니티</h1>
                {isLoggedIn && (
                    <button
                        className="potato-home-button"
                        onClick={() => navigate('/posts/create')}
                        style={{marginTop: '10px', color: '#F5F5F5'}}
                    >
                        게시글 작성
                    </button>
                )}
            </div>
            {loading && <div>로딩 중...</div>}
            {error && <div className="alert alert-danger">{error}</div>}
            <ul className="list-group">
                {posts.length === 0 && !loading && !error && (
                    <li className="list-group-item">게시글이 없습니다.</li>
                )}
                {posts.map(post => (
                    <li key={post.id} className="list-group-item">
                        <Link to={`/posts/${post.id}`} className="potato-link">
                            <h4>{post.title}</h4>
                            <div className="text-muted small mb-1">
                                작성자: {post.author} | 좋아요: {post.likeCount} | 조회수: {post.viewCount}
                            </div>
                        </Link>
                    </li>
                ))}
            </ul>
            <div className="potato-pagination-controls">
                <button
                    onClick={handlePrevPage}
                    disabled={pageInfo.first}
                    className="potato-btn"
                >
                    이전
                </button>
                <span className="potato-page-label">
                    페이지 {pageInfo.number + 1}
                </span>
                <input
                    type="text"
                    value={inputPage}
                    onChange={handleInputChange}
                    onKeyDown={handleInputKeyDown}
                    className="potato-page-input"
                    placeholder="번호"
                />
                <button
                    onClick={handleGoToPage}
                    className="potato-btn"
                >
                    이동
                </button>
                <button
                    onClick={handleNextPage}
                    disabled={pageInfo.last}
                    className="potato-btn"
                >
                    다음
                </button>
            </div>
        </div>
    );
};

export default PostList;
