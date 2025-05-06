package com.backendboard.global.security.handler;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.backendboard.global.security.dto.CustomOAuth2User;
import com.backendboard.global.util.JwtUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomOauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private final JwtUtil jwtUtil;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		System.out.println("=================Oauth2 로그인 성공 ============");

		CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();
		String username = customOAuth2User.getUsername();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();
		String role = auth.getAuthority();

		String accessToken = jwtUtil.createAccessToken(username, role);
		String refreshToken = jwtUtil.createRefreshToken(username, role);

		response.addCookie(jwtUtil.createAccessCookie(accessToken));
		response.addCookie(jwtUtil.createRefreshCookie(refreshToken));

		response.sendRedirect("http://localhost:80/");
	}
}
