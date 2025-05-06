package com.backendboard.global.util;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;

@Component
public class JwtUtil {
	private static final String USERNAME = "username";
	private static final String ROLE = "role";
	private static final String TYPE = "type";

	private final SecretKey secretKey;
	private final Long expiredAccessTokenTime;
	private final Long expiredRefreshTokenTime;

	public JwtUtil(@Value("${spring.jwt.secret}") String secret,
		@Value("${spring.jwt.expiration.access}") Long expiredAccessTokenTime,
		@Value("${spring.jwt.expiration.refresh}") Long expiredRefreshTokenTime) {
		this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
			Jwts.SIG.HS256.key().build().getAlgorithm());
		this.expiredAccessTokenTime = expiredAccessTokenTime;
		this.expiredRefreshTokenTime = expiredRefreshTokenTime;
	}

	public String getUsername(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get(USERNAME, String.class);
	}

	public String getRole(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get(ROLE, String.class);
	}

	public String getType(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get(TYPE, String.class);
	}

	public Boolean isExpired(String token) {
		try {
			Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getExpiration();
			return false;
		} catch (ExpiredJwtException exception) {
			return true;
		}
	}

	public String createAccessToken(String username, String role) {
		return Jwts.builder()
			.claim(USERNAME, username)
			.claim(ROLE, role)
			.claim(TYPE, "access")
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + expiredAccessTokenTime))
			.signWith(secretKey)
			.compact();
	}

	public String createRefreshToken(String username, String role) {
		return Jwts.builder()
			.claim(USERNAME, username)
			.claim(ROLE, role)
			.claim(TYPE, "refresh")
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + expiredRefreshTokenTime))
			.signWith(secretKey)
			.compact();
	}

	public String getToken(String authorization) {
		return authorization.split(" ")[1];
	}

	public boolean isNotBearerToken(String authorization) {
		return authorization == null || !authorization.startsWith("Bearer ");
	}

	public String extractedRefreshToken(Cookie[] cookies) {
		if (cookies == null) {
			return null;
		}
		return Arrays.stream(cookies)
			.filter(cookie -> cookie.getName().equals("refresh"))
			.map(Cookie::getValue)
			.findFirst()
			.orElse(null);
	}

	public Cookie createAccessCookie(String token) {
		Cookie cookie = new Cookie("access", token);
		cookie.setMaxAge((int)(expiredAccessTokenTime / 1000));
		cookie.setHttpOnly(false);
		cookie.setPath("/");
		return cookie;
	}

	public Cookie createRefreshCookie(String token) {
		Cookie cookie = new Cookie("refresh", token);
		cookie.setMaxAge((int)(expiredRefreshTokenTime / 1000));
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		return cookie;
	}

	public Cookie deleteRefreshCookie() {
		Cookie cookie = new Cookie("refresh", null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		return cookie;
	}
}
