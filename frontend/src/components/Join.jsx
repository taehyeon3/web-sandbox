// src/components/Join.jsx
import React, {useState} from 'react';
import {Alert, Button, Card, Col, Container, Form, Row} from 'react-bootstrap';
import potatoLogo from '../assets/potato-logo.png';

const Join = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [validated, setValidated] = useState(false);
    const [showError, setShowError] = useState(false);

    const handleSubmit = (event) => {
        event.preventDefault();
        const form = event.currentTarget;

        if (form.checkValidity() === false || password !== confirmPassword) {
            event.stopPropagation();
            setShowError(true);
        } else {
            console.log('회원가입 시도:', {email, password});
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
                                <img
                                    src={potatoLogo}
                                    alt="감자 로고"
                                    className="potato-logo"
                                    width="120"
                                />
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

                                <Button
                                    variant="primary"
                                    type="submit"
                                    className="potato-button w-100"
                                >
                                    회원가입
                                </Button>

                                <div className="text-center mt-3">
                                    이미 계정이 있으신가요?
                                    <a href="/login" className="potato-link">로그인</a>
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
