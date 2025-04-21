import React, {useEffect, useState} from 'react';
import {Link, useNavigate, useParams} from 'react-router-dom';
import api from "../../api/axiosInstance.jsx"
import '../../style/PostDetil.css'

const PostDetail = () => {
    const {id} = useParams();
    const [post, setPost] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const [isLiked, setIsLiked] = useState(false);
    const [likeCount, setLikeCount] = useState(0);
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    // 댓글 관련 상태
    const [comments, setComments] = useState([]);
    const [newComment, setNewComment] = useState('');
    const [commentLoading, setCommentLoading] = useState(false);
    const [currentPage, setCurrentPage] = useState(0);
    const [hasMore, setHasMore] = useState(true);
    const [editingComment, setEditingComment] = useState(null);
    const [editCommentText, setEditCommentText] = useState('');
    const commentsPerPage = 5;

    useEffect(() => {
        const user = localStorage.getItem('user');
        const accessToken = localStorage.getItem('accessToken');
        setIsLoggedIn(!!user && !!accessToken);
    }, []);

    useEffect(() => {
        api.get(`/posts/${id}`)
            .then(res => {
                if (res.status !== 200)
                    throw new Error('게시글을 불러올 수 없습니다.');
                return res.data;
            })
            .then(data => {
                setPost(data);
                setLikeCount(data.likeCount || 0);

                // 로그인한 경우 좋아요 상태 확인
                if (isLoggedIn) {
                    // 좋아요 상태 확인 로직 추가
                }
            })
            .catch(err => setError(err.message))
            .finally(() => setLoading(false));
    }, [id, isLoggedIn]);

    // 댓글 불러오기
    const fetchComments = (page = 0) => {
        setCommentLoading(true);
        api.get(`/comments/posts/${id}?page=${page}&size=${commentsPerPage}`)
            .then(res => {
                // 응답 데이터 구조 확인
                const responseData = res.data;
                const commentsList = responseData.content || responseData;

                if (page === 0) {
                    setComments(commentsList);
                } else {
                    setComments(prev => [...prev, ...commentsList]);
                }

                // 더 불러올 댓글이 있는지 확인 (페이지네이션 정보 활용)
                setHasMore(!res.data.empty);
            })
            .catch(err => {
                console.error('댓글을 불러오는데 실패했습니다:', err);
            })
            .finally(() => {
                setCommentLoading(false);
            });
    };

    // 초기 댓글 로드
    useEffect(() => {
        if (id) {
            fetchComments(0);
        }
    }, [id]);

    // 작성자와 현재 사용자가 일치하는지 확인하는 함수
    const isPostAuthor = () => {
        return true;
    };

    // 수정 페이지로 이동하는 핸들러
    const handleEdit = () => {
        navigate(`/posts/edit/${id}`);
    };

    // 좋아요 토글 핸들러
    const handleLikeToggle = async () => {
        if (!isLoggedIn) {
            alert('로그인이 필요한 기능입니다.');
            navigate('/login');
            return;
        }

        try {
            await api.post(`/post-likes/${id}`)

            // 좋아요 상태 및 카운트 업데이트
            setIsLiked(!isLiked);
            setLikeCount(prevCount => isLiked ? prevCount - 1 : prevCount + 1);
        } catch (err) {
            console.error('좋아요 처리 중 오류 발생:', err);
            alert('좋아요 처리 중 오류가 발생했습니다.');
        }
    };

    // 댓글 작성 핸들러
    const handleCommentSubmit = async (e) => {
        e.preventDefault();

        if (!isLoggedIn) {
            alert('로그인이 필요한 기능입니다.');
            navigate('/login');
            return;
        }

        if (!newComment.trim()) {
            alert('댓글 내용을 입력해주세요.');
            return;
        }

        setCommentLoading(true);

        try {
            const response = await api.post('/comments', {
                postId: id,
                content: newComment
            });

            // 새 댓글을 목록 맨 앞에 추가 (최신순 정렬 가정)
            setComments([response.data, ...comments]);
            setNewComment(''); // 입력창 초기화
        } catch (err) {
            console.error('댓글 작성 중 오류 발생:', err);
            alert('댓글 작성에 실패했습니다.');
        } finally {
            setCommentLoading(false);
        }
    };

    // 댓글 삭제 핸들러
    const handleDeleteComment = async (commentId) => {
        if (window.confirm('정말로 이 댓글을 삭제하시겠습니까?')) {
            try {
                await api.delete(`/comments/${commentId}`);
                // 삭제된 댓글 제외하고 목록 업데이트
                setComments(comments.filter(comment => comment.id !== commentId));
            } catch (err) {
                console.error('댓글 삭제 중 오류 발생:', err);
                alert('댓글 삭제에 실패했습니다.');
            }
        }
    };

    // 댓글 수정 모드 설정
    const handleEditComment = (comment) => {
        setEditingComment(comment.id);
        setEditCommentText(comment.content);
    };

    // 댓글 수정 취소
    const handleCancelEdit = () => {
        setEditingComment(null);
        setEditCommentText('');
    };

    // 댓글 수정 저장
    const handleSaveEdit = async (commentId) => {
        if (!editCommentText.trim()) {
            alert('댓글 내용을 입력해주세요.');
            return;
        }

        try {
            await api.put(`/comments/${commentId}`, {
                content: editCommentText
            });

            // 수정된 댓글로 업데이트
            setComments(comments.map(comment =>
                comment.id === commentId ? {...comment, content: editCommentText} : comment
            ));

            // 수정 모드 종료
            setEditingComment(null);
            setEditCommentText('');
        } catch (err) {
            console.error('댓글 수정 중 오류 발생:', err);
            alert('댓글 수정에 실패했습니다.');
        }
    };

    // 더 많은 댓글 불러오기
    const loadMoreComments = () => {
        if (!commentLoading && hasMore) {
            const nextPage = currentPage + 1;
            fetchComments(nextPage);
            setCurrentPage(nextPage);
        }
    };

    // 댓글 작성자인지 확인
    const isCommentAuthor = (comment) => {
        // 실제 구현에서는 현재 로그인한 사용자와 댓글 작성자를 비교해야 함
        return true; // 테스트를 위해 항상 true 반환
    };

    if (loading) return <div>로딩 중...</div>;
    if (error) return <div className="alert alert-danger">{error}</div>;
    if (!post) return <div>게시글이 존재하지 않습니다.</div>;

    return (
        <div className="potato-post-create-container">
            <div className="text-center mb-4">
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
            <div className="like-button-container">
                <button
                    onClick={handleLikeToggle}
                    className={`like-button ${isLiked ? 'liked' : ''}`}
                    disabled={!isLoggedIn}
                    title={isLoggedIn ? (isLiked ? '좋아요 취소' : '좋아요') : '로그인이 필요합니다'}
                >
                    {isLiked ? '❤️' : '🤍'} {likeCount}
                </button>
            </div>
            <div className="mt-4 text-end">
                {isPostAuthor() && (
                    <button
                        onClick={handleEdit}
                        className="potato-btn"
                        type="button"
                    >
                        수정하기
                    </button>
                )}
                <Link to="/posts" className="btn btn-outline-secondary">목록으로</Link>
            </div>
            {/* 댓글 섹션 */}
            <div className="comments-section mt-5">
                <h3 className="comments-title">댓글 {comments.length}개</h3>

                {/* 댓글 작성 폼 */}
                <div className="comment-form-container">
                    {isLoggedIn ? (
                        <form onSubmit={handleCommentSubmit} className="comment-form">
                            <textarea
                                value={newComment}
                                onChange={(e) => setNewComment(e.target.value)}
                                placeholder="댓글을 작성해주세요..."
                                className="comment-textarea"
                                disabled={commentLoading}
                                required
                            />
                            <button
                                type="submit"
                                className="comment-submit-btn"
                                disabled={commentLoading}
                            >
                                {commentLoading ? '등록 중...' : '댓글 등록'}
                            </button>
                        </form>
                    ) : (
                        <div className="login-required-message">
                            댓글을 작성하려면 <Link to="/login">로그인</Link>이 필요합니다.
                        </div>
                    )}
                </div>

                {/* 댓글 목록 */}
                <div className="comments-list">
                    {comments.length === 0 ? (
                        <p className="no-comments">아직 댓글이 없습니다. 첫 댓글을 작성해보세요!</p>
                    ) : (
                        <>
                            {comments.map(comment => (
                                <div key={comment.id} className="comment-item">
                                    <div className="comment-header">
                                        <span className="comment-author">{comment.author}</span>
                                        <span className="comment-date">
                                            {new Date(comment.createdAt).toLocaleString()}
                                        </span>
                                    </div>

                                    {editingComment === comment.id ? (
                                        <div className="comment-edit-form">
                                            <textarea
                                                value={editCommentText}
                                                onChange={(e) => setEditCommentText(e.target.value)}
                                                className="comment-edit-textarea"
                                            />
                                            <div className="comment-edit-actions">
                                                <button
                                                    onClick={() => handleSaveEdit(comment.id)}
                                                    className="comment-save-btn"
                                                >
                                                    저장
                                                </button>
                                                <button
                                                    onClick={handleCancelEdit}
                                                    className="comment-cancel-btn"
                                                >
                                                    취소
                                                </button>
                                            </div>
                                        </div>
                                    ) : (
                                        <div className="comment-content">{comment.content}</div>
                                    )}

                                    {isCommentAuthor(comment) && editingComment !== comment.id && (
                                        <div className="comment-actions">
                                            <button
                                                onClick={() => handleEditComment(comment)}
                                                className="comment-edit-btn"
                                            >
                                                수정
                                            </button>
                                            <button
                                                onClick={() => handleDeleteComment(comment.id)}
                                                className="comment-delete-btn"
                                            >
                                                삭제
                                            </button>
                                        </div>
                                    )}
                                </div>
                            ))}

                            {/* 더 불러오기 버튼 */}
                            {hasMore && (
                                <div className="load-more-container">
                                    <button
                                        onClick={loadMoreComments}
                                        className="load-more-btn"
                                        disabled={commentLoading}
                                    >
                                        {commentLoading ? '로딩 중...' : '댓글 더 보기'}
                                    </button>
                                </div>
                            )}
                        </>
                    )}
                </div>
            </div>
        </div>
    );
};

export default PostDetail;
