package com.backendboard.global.security.filter;

import java.io.IOException;

import org.springframework.web.filter.GenericFilterBean;

import com.backendboard.global.security.service.RefreshTokenService;
import com.backendboard.global.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {
	private final JwtUtil jwtUtil;
	private final RefreshTokenService refreshTokenService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		doFilter((HttpServletRequest)request, (HttpServletResponse)response, chain);
	}

	private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws IOException, ServletException {
		String requestUri = request.getRequestURI();
		String method = request.getMethod();

		if (!requestUri.matches("^/logout$") || !method.equals("POST")) {
			log.info("CustomLogoutFilter : CustomLogout 요청이 아님");
			filterChain.doFilter(request, response);
			return;
		}

		String refreshToken = jwtUtil.extractedRefreshToken(request.getCookies());

		if (jwtUtil.isExpired(refreshToken)) {
			log.info("CustomLogoutFilter : refreshToken이 만료됨");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		if (refreshToken == null || !jwtUtil.getType(refreshToken).equals("refresh")) {
			log.info("CustomLogoutFilter : refresh 토큰폼이 아님");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		if (!refreshTokenService.isValidRefreshToken(refreshToken)) {
			log.info("CustomLogoutFilter : refreshToken이 유효하지 않음");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		refreshTokenService.deleteRefreshToken(refreshToken);
		Cookie cookie = jwtUtil.deleteRefreshCookie();

		response.addCookie(cookie);
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
