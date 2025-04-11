// src/components/Join.jsx
import React, {useState} from 'react';
import {Alert, Button, Card, Col, Container, Form, Row} from 'react-bootstrap';
import LogoLink from "../components/LogoLink.jsx";
import {useNavigate} from "react-router-dom";

const Join = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [name, setName] = useState('');
    const [nickname, setNickname] = useState('');
    const [validated, setValidated] = useState(false);
    const [showError, setShowError] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        const form = event.currentTarget;

        if (form.checkValidity() === false || password !== confirmPassword) {
            event.stopPropagation();
            setShowError(true);
        } else {
            console.log('회원가입 시도:', {email, password});
            const userData = {
                loginId: email,
                password: password,
                username: name,
                nickname: nickname
            };
            // API 요청 보내기
            const response = await fetch('/api/join', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(userData)
            });

            if (response.ok) {
                const data = await response.json();
                console.log('회원가입 성공:', data);
                // 회원가입 성공 후 처리 (예: 로그인 페이지로 이동)
                navigate('/login');
            } else {
                console.error('회원가입 실패');
                setShowError(true);
            }
            setShowError(false);
        }

        setValidated(true);
    };

    return (
        <Container className="potato-signup-container">
            <Row className="justify-content-md-center">
                <Col xs={12} md={6}>
                    <Card className="potato-card">
                        <Card.Body>
                            <div className="text-center mb-4">
                                <LogoLink width="150"/>
                                <h2 className="potato-title">회원가입</h2>
                                <p className="potato-subtitle">맛있는 감자의 세계에 함께하세요!</p>
                            </div>

                            {showError && (
                                <Alert variant="danger">
                                    모든 필드를 올바르게 입력해주세요.
                                </Alert>
                            )}

                            <Form noValidate validated={validated} onSubmit={handleSubmit}>
                                <Form.Group className="mb-3" controlId="formBasicEmail">
                                    <Form.Label>이메일</Form.Label>
                                    <Form.Control
                                        type="email"
                                        placeholder="potato@example.com"
                                        value={email}
                                        onChange={(e) => setEmail(e.target.value)}
                                        required
                                    />
                                    <Form.Control.Feedback type="invalid">
                                        유효한 이메일을 입력해주세요.
                                    </Form.Control.Feedback>
                                </Form.Group>

                                <Form.Group className="mb-3" controlId="formBasicPassword">
                                    <Form.Label>비밀번호</Form.Label>
                                    <Form.Control
                                        type="password"
                                        placeholder="비밀번호"
                                        value={password}
                                        onChange={(e) => setPassword(e.target.value)}
                                        required
                                        minLength="6"
                                    />
                                    <Form.Control.Feedback type="invalid">
                                        비밀번호는 최소 6자 이상이어야 합니다.
                                    </Form.Control.Feedback>
                                </Form.Group>

                                <Form.Group className="mb-3" controlId="formBasicConfirmPassword">
                                    <Form.Label>비밀번호 확인</Form.Label>
                                    <Form.Control
                                        type="password"
                                        placeholder="비밀번호 확인"
                                        value={confirmPassword}
                                        onChange={(e) => setConfirmPassword(e.target.value)}
                                        required
                                        minLength="6"
                                        isInvalid={password !== confirmPassword}
                                    />
                                    <Form.Control.Feedback type="invalid">
                                        비밀번호가 일치하지 않습니다.
                                    </Form.Control.Feedback>
                                </Form.Group>

                                <Form.Group className="mb-3" controlId="formName">
                                    <Form.Label>이름</Form.Label>
                                    <Form.Control
                                        type="name"
                                        placeholder="홍길동"
                                        value={name}
                                        onChange={(e) => setName(e.target.value)}
                                        required
                                        minLength="1"
                                        isInvalid={password !== confirmPassword}
                                    />
                                    <Form.Control.Feedback type="invalid">
                                        이름을 입력해주세요.
                                    </Form.Control.Feedback>
                                </Form.Group>

                                <Form.Group className="mb-3" controlId="formNickname">
                                    <Form.Label>닉네임</Form.Label>
                                    <Form.Control
                                        type="nickname"
                                        placeholder="감자"
                                        value={nickname}
                                        onChange={(e) => setNickname(e.target.value)}
                                        required
                                        minLength="1"
                                        isInvalid={password !== confirmPassword}
                                    />
                                    <Form.Control.Feedback type="invalid">
                                        닉네임을 입력해주세요.
                                    </Form.Control.Feedback>
                                </Form.Group>

                                <Button
                                    variant="primary"
                                    type="submit"
                                    className="potato-button w-100"
                                >
                                    회원가입
                                </Button>

                                <div className="text-center mt-3">
                                    이미 계정이 있으신가요?
                                    <a href="/Login" className="potato-link">로그인</a>
                                </div>
                            </Form>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
};

export default Join;
