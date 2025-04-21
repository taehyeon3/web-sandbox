package com.backendboard.global.security.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.backendboard.global.security.dto.CustomUserDetails;
import com.backendboard.global.security.service.CustomUserDetailsService;
import com.backendboard.global.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	private static final String AUTHORIZATION = "Authorization";

	private final CustomUserDetailsService customUserDetailsService;
	private final JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		String authorization = request.getHeader(AUTHORIZATION);

		if (jwtUtil.isNotBearerToken(authorization)) {
			log.info("JwtFilter : 잘못된 토큰");
			filterChain.doFilter(request, response);
			return;
		}

		String token = jwtUtil.getToken(authorization);

		if (jwtUtil.isExpired(token)) {
			log.info("JwtFilter : 토큰 만료");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		if (!jwtUtil.getType(token).equals("access")) {
			log.info("JwtFilter : access 토큰이 아님");
			filterChain.doFilter(request, response);
			return;
		}

		String username = jwtUtil.getUsername(token);
		CustomUserDetails customUserDetails = (CustomUserDetails)customUserDetailsService.loadUserByUsername(username);
		Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null,
			customUserDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authToken);
		filterChain.doFilter(request, response);
	}
}




