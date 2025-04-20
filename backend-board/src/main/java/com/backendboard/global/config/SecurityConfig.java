package com.backendboard.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import com.backendboard.global.security.filter.CustomLogoutFilter;
import com.backendboard.global.security.filter.JwtFilter;
import com.backendboard.global.security.filter.LoginFilter;
import com.backendboard.global.security.service.CustomUserDetailsService;
import com.backendboard.global.security.service.RefreshTokenService;
import com.backendboard.global.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final AuthenticationConfiguration authenticationConfiguration;
	private final CustomUserDetailsService customUserDetailsService;
	private final JwtUtil jwtUtil;
	private final RefreshTokenService refreshTokenService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests((auth) -> auth
				.requestMatchers(HttpMethod.GET, "/**").permitAll()
				.requestMatchers("/login", "/join", "/reissue", "/swagger-ui/**", "/swagger-resources/**",
					"/v3/api-docs/**")
				.permitAll()
				.anyRequest()
				.authenticated())
			.addFilterBefore(new JwtFilter(customUserDetailsService, jwtUtil), LoginFilter.class)
			.addFilterAt(
				new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshTokenService),
				UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshTokenService), LogoutFilter.class)
			.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.build();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
}
