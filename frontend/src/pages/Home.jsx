import React from 'react';
import {Button, Card, Col, Container, Row} from 'react-bootstrap';
import {Link, useNavigate} from 'react-router-dom';
import LogoLink from "../components/LogoLink.jsx";

const Home = () => {
    const isLoggedIn = localStorage.getItem('user') !== null;
    const navigate = useNavigate();
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
