package com.backendboard.domain.auth.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RefreshToken {
	private final String refreshToken;

	private final String username;

	@Builder
	private RefreshToken(String refreshToken, String username) {
		this.refreshToken = refreshToken;
		this.username = username;
	}
}
