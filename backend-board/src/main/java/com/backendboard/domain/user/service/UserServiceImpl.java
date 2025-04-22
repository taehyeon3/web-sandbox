package com.backendboard.domain.user.service;

import org.springframework.stereotype.Service;

import com.backendboard.domain.user.dto.UserReadResponse;
import com.backendboard.domain.user.entity.User;
import com.backendboard.domain.user.repository.UserRepository;
import com.backendboard.global.error.CustomError;
import com.backendboard.global.error.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;

	@Override
	public UserReadResponse getUser(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(CustomError.USER_NOT_FOUND));
		return UserReadResponse.toDto(user);
	}
}
