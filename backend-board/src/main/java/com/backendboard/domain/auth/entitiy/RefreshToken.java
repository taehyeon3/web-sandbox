package com.backendboard.domain.auth.entitiy;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;
import lombok.Getter;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 43200)
public class RefreshToken {
	@Id
	private String refreshToken;

	private String username;

	@Builder
	private RefreshToken(String refreshToken, String username) {
		this.refreshToken = refreshToken;
		this.username = username;
	}
}
