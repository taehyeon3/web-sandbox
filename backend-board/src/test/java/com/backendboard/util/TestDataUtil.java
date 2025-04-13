package com.backendboard.util;

import org.springframework.stereotype.Component;

import com.backendboard.domain.auth.entity.AuthUser;
import com.backendboard.domain.auth.entity.type.UserRole;
import com.backendboard.domain.auth.repository.AuthUserRepository;
import com.backendboard.domain.user.entity.User;
import com.backendboard.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TestDataUtil {
	private final AuthUserRepository authUserRepository;
	private final UserRepository userRepository;

	public AuthUser createAuthUser(String username, String password, UserRole role) {
		AuthUser authUser = AuthUser.builder().username(username).password(password).role(role).build();
		return authUserRepository.save(authUser);
	}

	public User createUser(AuthUser authUser, String username, String nickname) {
		User user = authUser.createUser(username, nickname);
		return userRepository.save(user);
	}
}
