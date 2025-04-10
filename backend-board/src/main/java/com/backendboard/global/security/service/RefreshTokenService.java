package com.backendboard.global.security.service;

import org.springframework.stereotype.Service;

import com.backendboard.domain.auth.dto.RefreshTokenDto;
import com.backendboard.domain.auth.entitiy.RefreshToken;
import com.backendboard.domain.auth.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
	private final RefreshTokenRepository refreshTokenRepository;

	public void deleteRefreshToken(String refreshToken) {
		refreshTokenRepository.delete(refreshToken);
	}

	public void saveRefreshToken(RefreshTokenDto tokenDto) {
		RefreshToken refreshToken = RefreshTokenDto.toEntity(tokenDto);
		refreshTokenRepository.save(refreshToken);
	}

	public boolean isValidRefreshToken(String refreshToken) {
		return refreshTokenRepository.exists(refreshToken);
	}
}
