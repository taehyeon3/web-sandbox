// src/components/Login.jsx
import React, {useState} from 'react';
import {Alert, Button, Card, Col, Container, Form, Row} from 'react-bootstrap';
import LogoLink from "../components/LogoLink.jsx";
import {useNavigate} from "react-router-dom";
import api from "../api/axiosInstance.jsx";

const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [validated, setValidated] = useState(false);
    const [showError, setShowError] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        const form = event.currentTarget;

        if (form.checkValidity() === false) {
            event.stopPropagation();
            setShowError(true);
        } else {
            console.log('로그인 시도:', {email, password});
            const formData = new FormData();
            formData.append('username', email);
            formData.append('password', password);
            // API 요청 보내기
            const response = await fetch('/api/login', {
                method: 'POST',
                body: formData
                // FormData를 사용할 때는 Content-Type 헤더를 설정하지 않습니다.
                // 브라우저가 자동으로 multipart/form-data로 설정합니다.
            });
            if (response.ok) {
                localStorage.setItem('accessToken', response.headers.get('Authorization'));
                api.get('users').then(response => {
                    if (response.status === 200) {
                        console.log("성공적으로 데이터를 가져왔습니다.");
                        localStorage.setItem('user', JSON.stringify({
                            id: response.data.id,
                            email,
                            name: response.data.name,
                            nickname: response.data.nickname
                        }));
                        console.log('로그인 성공:');
                        navigate('/');
                    } else {
                        console.log(`오류 발생: ${response.status}`);
                    }
                });
            } else {
                console.error('로그인 실패');
                setShowError(true);
            }
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
                                <LogoLink width="150"/>
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
                                        <a href="/Join" className="potato-link">회원가입</a>
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
