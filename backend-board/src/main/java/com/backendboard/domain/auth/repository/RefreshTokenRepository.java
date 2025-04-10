package com.backendboard.domain.auth.repository;

import com.backendboard.domain.auth.entitiy.RefreshToken;

public interface RefreshTokenRepository {
	void save(RefreshToken refreshToken);

	void delete(String refreshToken);

	boolean exists(String refreshToken);
}
