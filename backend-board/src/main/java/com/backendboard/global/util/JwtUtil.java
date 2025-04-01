package com.backendboard.global.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

@Component
public class JwtUtil {
	private static final String USERNAME = "username";
	private static final String ROLE = "role";

	private final SecretKey secretKey;
	private final Long expiredTimeMillis;

	public JwtUtil(@Value("${spring.jwt.secret}") String secret,
		@Value("${spring.jwt.expiration.access}") Long expiredTimeMillis) {
		this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
			Jwts.SIG.HS256.key().build().getAlgorithm());
		this.expiredTimeMillis = expiredTimeMillis;
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

	public String createToken(String username, String role) {
		return Jwts.builder()
			.claim(USERNAME, username)
			.claim(ROLE, role)
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + expiredTimeMillis))
			.signWith(secretKey)
			.compact();
	}

	public String getToken(String authorization) {
		return authorization.split(" ")[1];
	}

	public boolean isBearerToken(String authorization) {
		return authorization == null || !authorization.startsWith("Bearer ");
	}
}
