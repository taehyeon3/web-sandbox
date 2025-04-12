package com.backendboard.global.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.backendboard.domain.auth.entity.AuthUser;
import com.backendboard.domain.auth.repository.AuthUserRepository;
import com.backendboard.global.error.CustomError;
import com.backendboard.global.security.dto.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final AuthUserRepository authUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AuthUser authUser = authUserRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException(CustomError.AUTH_NOT_FOUND_ID.getMessage()));
		return new CustomUserDetails(authUser);
	}
}
