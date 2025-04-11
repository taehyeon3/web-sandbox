import React from 'react';
import {BrowserRouter, Navigate, Route, Routes} from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import './index.css';
import Home from "./components/Home.jsx";
import Login from "./components/Login.jsx";
import Join from "./components/Join.jsx";


const Router = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Home/>}/>
                <Route path="/login" element={<Login onNavigate={'login'}/>}/>
                <Route path="/join" element={<Join onNavigate={'join'}/>}/>
                <Route path="*" element={<Navigate to="/" replace/>}/>
            </Routes>
        </BrowserRouter>
    );
};

export default Router;
