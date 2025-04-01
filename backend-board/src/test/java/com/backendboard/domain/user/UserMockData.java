package com.backendboard.domain.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.backendboard.domain.user.dto.JoinRequest;
import com.backendboard.domain.user.entitiy.AuthUser;
import com.backendboard.domain.user.entitiy.User;
import com.backendboard.domain.user.entitiy.type.UserRole;
import com.backendboard.domain.user.repository.AuthUserRepository;
import com.backendboard.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMockData {
	private final AuthUserRepository authUserRepository;
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public User createUserAndAuthUser(JoinRequest request) {
		AuthUser authUser = new AuthUser(request.getLoginId(),
			bCryptPasswordEncoder.encode(request.getPassword()), UserRole.USER);
		User user = authUser.createUser(request.getUsername(), request.getNickname());
		authUserRepository.save(authUser);
		return userRepository.save(user);
	}
}
