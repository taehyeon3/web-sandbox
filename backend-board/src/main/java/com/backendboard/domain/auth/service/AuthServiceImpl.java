package com.backendboard.domain.auth.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backendboard.domain.auth.dto.JoinRequest;
import com.backendboard.domain.auth.dto.JoinResponse;
import com.backendboard.domain.auth.dto.PasswordUpdateRequest;
import com.backendboard.domain.auth.dto.RefreshTokenDto;
import com.backendboard.domain.auth.entity.AuthUser;
import com.backendboard.domain.auth.entity.RefreshToken;
import com.backendboard.domain.auth.entity.type.UserRole;
import com.backendboard.domain.auth.repository.AuthUserRepository;
import com.backendboard.domain.auth.repository.RefreshTokenRepository;
import com.backendboard.domain.user.entity.User;
import com.backendboard.domain.user.repository.UserRepository;
import com.backendboard.global.error.CustomError;
import com.backendboard.global.error.CustomException;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final UserRepository userRepository;
	private final AuthUserRepository authUserRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public void deleteRefreshToken(String refreshToken) {
		refreshTokenRepository.delete(refreshToken);
	}

	@Override
	public void saveRefreshToken(RefreshTokenDto tokenDto) {
		RefreshToken refreshToken = RefreshTokenDto.toEntity(tokenDto);
		refreshTokenRepository.save(refreshToken);
	}

	@Override
	public boolean isValidRefreshToken(String refreshToken) {
		return refreshTokenRepository.exists(refreshToken);
	}

	@Transactional
	@Override
	public JoinResponse joinProcess(JoinRequest request) {
		validateDuplicationId(request.getLoginId());
		validateDuplicationNickname(request.getNickname());

		AuthUser authUser = AuthUser.create(request.getLoginId(),
			bCryptPasswordEncoder.encode(request.getPassword()), UserRole.USER);
		User user = authUser.createUser(request.getUsername(), request.getNickname());
		authUserRepository.save(authUser);
		userRepository.save(user);
		return JoinResponse.toDto(user);
	}

	@Transactional
	@Override
	public void updatePassword(PasswordUpdateRequest request, Long authUserId) {
		AuthUser user = authUserRepository.getReferenceById(authUserId);
		user.updatePassword(bCryptPasswordEncoder.encode(request.getPassword()));
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
