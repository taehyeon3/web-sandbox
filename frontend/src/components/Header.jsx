// src/components/Header.jsx
import React from 'react';
import {Button, Container, Navbar} from 'react-bootstrap';
import {Link, useNavigate} from 'react-router-dom';
import potatoLogo from "../assets/potato-logo.png";

const Header = () => {
    const navigate = useNavigate();

    // 로컬 스토리지에서 로그인 상태 확인
    const isLoggedIn = localStorage.getItem('user') !== null;

    const handleLogout = () => {
        // 로컬 스토리지에서 사용자 정보 삭제
        localStorage.removeItem('user');
        // 홈페이지로 이동
        navigate('/');
        // 페이지 새로고침 (선택사항)
        window.location.reload();
    };

    return (
        <Navbar bg="light" expand="lg" className="potato-navbar">
            <Container>
                <img
                    src={potatoLogo}
                    alt="감자 로고"
                    className="potato-logo"
                    width={50}
                />
                <Navbar.Brand as={Link} to="/" className="potato-title"
                              style={{color: '#a67c52', fontWeight: 'bold', fontSize: '2rem', marginLeft: '20px'}}>
                    감자 나라
                </Navbar.Brand>
                <div className="ms-auto">
                    {isLoggedIn && (
                        <Button
                            variant="outline-danger"
                            onClick={handleLogout}
                        >
                            로그아웃
                        </Button>
                    )}
                </div>
            </Container>
        </Navbar>
    );
};

export default Header;
