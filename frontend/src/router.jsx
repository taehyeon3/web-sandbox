import React from 'react';
import {BrowserRouter, Navigate, Route, Routes} from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import './index.css';
import Home from "./pages/Home.jsx";
import Login from "./pages/Login.jsx";
import Join from "./pages/Join.jsx";
import Header from "./components/Header.jsx";
import PostList from "./pages/PostList.jsx";
import PostDetail from "./pages/PostDetail.jsx";

const Router = () => {
    return (
        <BrowserRouter>
            <Header/>
            <div className="content-container">
                <Routes>
                    <Route path="/" element={<Home/>}/>
                    <Route path="/login" element={<Login/>}/>
                    <Route path="/join" element={<Join/>}/>
                    <Route path="/posts" element={<PostList/>}/>
                    <Route path="/posts/:id" element={<PostDetail/>}/>
                    <Route path="*" element={<Navigate to="/" replace/>}/>
                </Routes>
            </div>
        </BrowserRouter>
    );
};

export default Router;
