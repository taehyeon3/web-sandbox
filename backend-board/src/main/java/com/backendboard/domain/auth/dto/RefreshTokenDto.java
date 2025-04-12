package com.backendboard.domain.auth.dto;

import com.backendboard.domain.auth.entity.RefreshToken;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RefreshTokenDto {
	private final String username;

	private final String refreshToken;

	@Builder
	private RefreshTokenDto(String username, String refreshToken) {
		this.username = username;
		this.refreshToken = refreshToken;
	}

	public static RefreshTokenDto toDto(String username, String refreshToken) {
		return RefreshTokenDto.builder()
			.username(username)
			.refreshToken(refreshToken)
			.build();
	}

	public static RefreshToken toEntity(RefreshTokenDto dto) {
		return RefreshToken.builder()
			.username(dto.getUsername())
			.refreshToken(dto.getRefreshToken())
			.build();
	}
}
