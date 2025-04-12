package com.backendboard.domain.auth.repository;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.backendboard.domain.auth.entity.RefreshToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
	private static final String KEY_PREFIX = "refresh:";

	private final RedisTemplate<String, Object> redisTemplate;

	@Value("${spring.jwt.expiration.refresh}")
	private Long expiredRefreshTokenTime;

	@Override
	public void save(RefreshToken refreshToken) {
		redisTemplate.opsForValue().set(
			KEY_PREFIX + refreshToken.getRefreshToken(), refreshToken.getUsername(), expiredRefreshTokenTime,
			TimeUnit.MILLISECONDS);
	}

	@Override
	public void delete(String refreshToken) {
		redisTemplate.delete(KEY_PREFIX + refreshToken);
	}

	@Override
	public boolean exists(String refreshToken) {
		Boolean hasKey = redisTemplate.hasKey(KEY_PREFIX + refreshToken);
		return hasKey != null && hasKey;
	}
}
