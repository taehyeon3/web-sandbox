package com.backendboard.domain.auth.service;

import com.backendboard.domain.auth.dto.JoinRequest;
import com.backendboard.domain.auth.dto.JoinResponse;
import com.backendboard.domain.auth.dto.RefreshTokenDto;

public interface AuthService {
	JoinResponse joinProcess(JoinRequest request);

	void saveRefreshToken(RefreshTokenDto tokenDto);

	void deleteRefreshToken(String username);
}
