import React, {useEffect, useState} from 'react';
import {Button, Card, Col, Container, Row} from 'react-bootstrap';
import {Link, useNavigate} from 'react-router-dom';
import LogoLink from "../components/LogoLink.jsx";
import api from "../api/axiosInstance.jsx";

const Home = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(localStorage.getItem('user') !== null);
    const navigate = useNavigate();

    // 쿠키에서 값을 가져오는 함수
    const getCookie = (name) => {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(';').shift();
        return null;
    };

    // 쿠키 삭제 함수
    const deleteCookie = (name) => {
        document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`;
    };

    useEffect(() => {
        // 쿠키에서 accessToken 확인
        const accessToken = getCookie('access');

        if (accessToken) {
            console.log('쿠키에서 accessToken을 찾았습니다.');

            // Authorization 헤더에 토큰 추가
            api.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;
            localStorage.setItem('accessToken', `Bearer ${accessToken}`);

            // 사용자 정보 가져오기
            api.get('users')
                .then(response => {
                    if (response.status === 200) {
                        console.log("성공적으로 데이터를 가져왔습니다.");

                        // 사용자 정보 localStorage에 저장
                        localStorage.setItem('user', JSON.stringify({
                            id: response.data.id,
                            email: response.data.email, // 이메일 정보도 response에서 가져옴
                            name: response.data.name,
                            nickname: response.data.nickname
                        }));

                        // 로그인 상태 업데이트
                        setIsLoggedIn(true);

                        // 쿠키에서 accessToken 삭제
                        deleteCookie('access');
                        console.log('쿠키에서 accessToken을 삭제했습니다.');
                    }
                })
                .catch(error => {
                    console.error('사용자 정보를 가져오는 중 오류 발생:', error);
                    // 오류 발생 시에도 쿠키 삭제
                    deleteCookie('access');
                });
        }
    }, []);

    return (
        <Container className="py-5">
            <Row className="justify-content-center">
                <Col md={8} className="text-center">
                    <LogoLink width="150"/>
                    <h1 className="potato-title"> 🥔 감자 나라 🥔</h1>
                    <p className="potato-subtitle">
                        감자 월드는 감자를 사랑하는 모든 사람들을 위한 커뮤니티입니다.
                    </p>

                    {!isLoggedIn && (
                        <div className="d-flex justify-content-center gap-3 mb-5">
                            <Button as={Link} to="/login" variant="outline-warning" size="lg">
                                로그인
                            </Button>
                            <Button as={Link} to="/join" variant="outline-warning" size="lg">
                                회원가입
                            </Button>
                        </div>
                    )}

                    <Row className="mt-5">
                        <Col md={4}>
                            <Card className="mb-4 potato-feature-card">
                                <Card.Body>
                                    <h3>🍟 광고 문의</h3>
                                    <p>광고를 올려주세요!</p>
                                </Card.Body>
                            </Card>
                        </Col>
                        <Col md={4}>
                            <Card className="mb-4 potato-feature-card">
                                <Card.Body>
                                    <h3>🌱 광고 문의</h3>
                                    <p>광고를 올려주세요!!</p>
                                </Card.Body>
                            </Card>
                        </Col>
                        <Col md={4}>
                            <Card
                                className="mb-4 potato-feature-card"
                                style={{cursor: 'pointer'}}
                                onClick={() => navigate('/posts')}
                            >
                                <Card.Body>
                                    <h3>👨‍🌾 커뮤니티</h3>
                                    <p>사람들과 소통해보세요!</p>
                                </Card.Body>
                            </Card>
                        </Col>
                    </Row>
                </Col>
            </Row>
        </Container>
    );
};

export default Home;
