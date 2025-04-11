// src/components/Login.jsx
import React, {useState} from 'react';
import {Alert, Button, Card, Col, Container, Form, Row} from 'react-bootstrap';
import potatoLogo from '../assets/potato-logo.png';

const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [validated, setValidated] = useState(false);
    const [showError, setShowError] = useState(false);

    const handleSubmit = (event) => {
        event.preventDefault();
        const form = event.currentTarget;

        if (form.checkValidity() === false) {
            event.stopPropagation();
            setShowError(true);
        } else {
            console.log('로그인 시도:', {email, password});
            setShowError(false);
        }

        setValidated(true);
    };

    return (
        <Container className="potato-login-container">
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
                                <h2 className="potato-title">로그인</h2>
                                <p className="potato-subtitle">맛있는 감자의 세계에 오신 것을 환영합니다!</p>
                            </div>

                            {showError && (
                                <Alert variant="danger">
                                    이메일과 비밀번호를 올바르게 입력해주세요.
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

                                <Form.Group className="mb-3" controlId="formBasicCheckbox">
                                    <Form.Check
                                        type="checkbox"
                                        label="로그인 상태 유지"
                                        className="potato-checkbox"
                                    />
                                </Form.Group>

                                <Button
                                    variant="primary"
                                    type="submit"
                                    className="potato-button w-100"
                                >
                                    로그인
                                </Button>

                                <div className="text-center mt-3">
                                    <a href="#" className="potato-link">비밀번호를 잊으셨나요?</a>
                                    <div className="mt-2">
                                        계정이 없으신가요?
                                        <a href="/join" className="potato-link">회원가입</a>
                                    </div>
                                </div>
                            </Form>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
};

export default Login;
