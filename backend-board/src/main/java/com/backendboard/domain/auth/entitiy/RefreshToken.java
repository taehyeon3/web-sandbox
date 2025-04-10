package com.backendboard.domain.auth.entitiy;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RefreshToken {
	private String refreshToken;

	private String username;

	@Builder
	private RefreshToken(String refreshToken, String username) {
		this.refreshToken = refreshToken;
		this.username = username;
	}
}
