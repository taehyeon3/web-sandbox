package com.backendboard.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backendboard.domain.user.dto.UserInfoResponse;
import com.backendboard.domain.user.dto.UserNicknameUpdateRequest;
import com.backendboard.domain.user.dto.UserNicknameUpdateResponse;
import com.backendboard.domain.user.entity.User;
import com.backendboard.domain.user.repository.UserRepository;
import com.backendboard.global.error.CustomError;
import com.backendboard.global.error.CustomException;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;

	@Override
	public UserInfoResponse getInfo(Long authUserId) {
		User user = userRepository.getByAuthUserId(authUserId);
		return UserInfoResponse.toDto(user);
	}

	@Transactional
	@Override
	public UserNicknameUpdateResponse updateNickname(UserNicknameUpdateRequest request, Long authUserId) {
		User user = userRepository.getByAuthUserId(authUserId);
		//공백 제거
		String updateNickname = request.getNickname().replaceAll("\\p{Z}", "");

		validateDuplicationNickname(updateNickname);

		user.updateNickname(updateNickname);
		return UserNicknameUpdateResponse.toDto(user);
	}

	private void validateDuplicationNickname(String nickname) {
		if (userRepository.existsByNickname(nickname)) {
			throw new CustomException(CustomError.USER_DUPLICATION_NICKNAME);
		}
	}
}
