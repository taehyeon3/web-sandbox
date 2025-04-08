package com.backendboard.domain.user.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backendboard.domain.user.dto.JoinRequest;
import com.backendboard.domain.user.dto.JoinResponse;
import com.backendboard.domain.user.entitiy.AuthUser;
import com.backendboard.domain.user.entitiy.User;
import com.backendboard.domain.user.entitiy.type.UserRole;
import com.backendboard.domain.user.repository.AuthUserRepository;
import com.backendboard.domain.user.repository.UserRepository;
import com.backendboard.global.error.CustomError;
import com.backendboard.global.error.CustomException;
import com.backendboard.global.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final UserRepository userRepository;
	private final AuthUserRepository authUserRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final JwtUtil jwtUtil;

	@Override
	public String reissueProcess(String refreshToken) {
		validateRefreshToken(refreshToken);
		String username = jwtUtil.getUsername(refreshToken);
		String role = jwtUtil.getRole(refreshToken);
		return jwtUtil.createAccessToken(username, role);
	}

	private void validateRefreshToken(String refreshToken) {
		if (jwtUtil.getType(refreshToken).equals("refresh") || jwtUtil.isExpired(refreshToken)) {
			throw new CustomException(CustomError.AUTH_INVALID_TOKEN);
		}
	}

	@Transactional
	@Override
	public JoinResponse joinProcess(JoinRequest request) {
		validateDuplicationId(request.getLoginId());
		validateDuplicationNickname(request.getNickname());

		AuthUser authUser = new AuthUser(request.getLoginId(),
			bCryptPasswordEncoder.encode(request.getPassword()), UserRole.USER);
		User user = authUser.createUser(request.getUsername(), request.getNickname());
		authUserRepository.save(authUser);
		userRepository.save(user);
		return JoinResponse.toDto(user);
	}

	public void validateDuplicationNickname(String nickname) {
		if (userRepository.existsByNickname(nickname)) {
			throw new CustomException(CustomError.USER_DUPLICATION_NICKNAME);
		}
	}

	public void validateDuplicationId(String loginId) {
		if (authUserRepository.existsByUsername(loginId)) {
			throw new CustomException(CustomError.USER_DUPLICATION_ID);
		}
	}
}
