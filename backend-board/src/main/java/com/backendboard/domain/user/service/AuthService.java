package com.backendboard.domain.user.service;

import com.backendboard.domain.user.dto.JoinRequest;
import com.backendboard.domain.user.dto.JoinResponse;

public interface AuthService {
	JoinResponse joinProcess(JoinRequest request);

	String reissueProcess(String refreshToken);
}
