package com.backendboard.domain.auth.entitiy;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;
import lombok.Getter;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 43200)
public class RefreshToken {
	@Id
	private String username;

	private String refreshToken;

	@Builder
	private RefreshToken(String username, String refreshToken) {
		this.username = username;
		this.refreshToken = refreshToken;
	}
}
