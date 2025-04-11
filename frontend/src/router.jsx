import React, {useState} from 'react';
import {BrowserRouter, Navigate, Route, Routes} from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import './index.css';
import Home from "./pages/Home.jsx";
import Login from "./pages/Login.jsx";
import Join from "./pages/Join.jsx";
import Header from "./components/Header.jsx";

const Router = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    return (
        <BrowserRouter>
            <Header/>
            <div className="content-container">
                <Routes>
                    <Route path="/" element={<Home/>}/>
                    <Route path="/login" element={<Login/>}/>
                    <Route path="/join" element={<Join/>}/>
                    <Route path="*" element={<Navigate to="/" replace/>}/>
                </Routes>
            </div>
        </BrowserRouter>
    );
};

export default Router;
