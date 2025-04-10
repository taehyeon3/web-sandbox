package com.backendboard.global.security.filter;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.backendboard.domain.auth.dto.RefreshTokenDto;
import com.backendboard.global.security.dto.CustomUserDetails;
import com.backendboard.global.security.service.RefreshTokenService;
import com.backendboard.global.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final RefreshTokenService refreshTokenService;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {
		String username = obtainUsername(request);
		String password = obtainPassword(request);

		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password,
			null);
		return authenticationManager.authenticate(authToken);
	}

	//로그인 성공
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authentication) {
		CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();
		String username = customUserDetails.getUsername();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();
		String role = auth.getAuthority();

		String accessToken = jwtUtil.createAccessToken(username, role);
		String refreshToken = jwtUtil.createRefreshToken(username, role);
		RefreshTokenDto tokenDto = RefreshTokenDto.toDto(username, refreshToken);

		refreshTokenService.saveRefreshToken(tokenDto);
		response.addHeader("Authorization", "Bearer " + accessToken);
		response.addCookie(jwtUtil.createRefreshCookie(refreshToken));
		response.setStatus(HttpStatus.OK.value());
	}

	//로그인 실패
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException failed) {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
	}
}
