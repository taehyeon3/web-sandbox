// src/api/axiosInstance.js
import axios from "axios";

// 1. axios 인스턴스 생성
const api = axios.create({
    baseURL: "/api", // 필요시 수정
    withCredentials: true,
    // timeout 등 옵션 추가 가능
});

// 2. 요청 인터셉터: accessToken 자동 추가
api.interceptors.request.use(
    (config) => {
        const accessToken = localStorage.getItem("accessToken");
        if (accessToken) {
            config.headers.Authorization = `${accessToken}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// 3. 응답 인터셉터: accessToken 만료(401) 시 refreshToken으로 재발급
let isRefreshing = false;
let refreshSubscribers = [];

function onRefreshed(newAccessToken) {
    refreshSubscribers.forEach((callback) => callback(newAccessToken));
    refreshSubscribers = [];
}

function addRefreshSubscriber(callback) {
    refreshSubscribers.push(callback);
}

api.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;
        // accessToken 만료(401) && 재발급 시도가 아닌 경우
        if (
            error.response &&
            error.response.status === 401 &&
            !originalRequest._retry
        ) {
            originalRequest._retry = true;

            // 중복 재발급 방지
            if (!isRefreshing) {
                isRefreshing = true;
                try {
                    const res = await axios.post("/api/reissue", {}, {
                        withCredentials: true // 핵심
                    });
                    const newAccessToken = res.headers['authorization'];
                    localStorage.setItem("accessToken", newAccessToken);
                    isRefreshing = false;
                    onRefreshed(newAccessToken);
                } catch (refreshError) {
                    isRefreshing = false;
                    // 재발급 실패: 로그아웃 등 처리
                    localStorage.removeItem("user");
                    localStorage.removeItem("accessToken");
                    window.location.href = "/login";
                    return Promise.reject(refreshError);
                }
            }

            // 재발급이 끝날 때까지 대기 후 원래 요청 재시도
            return new Promise((resolve) => {
                addRefreshSubscriber((newAccessToken) => {
                    originalRequest.headers.Authorization = `${newAccessToken}`;
                    resolve(api(originalRequest));
                });
            });
        }
        return Promise.reject(error);
    }
);

export default api;
