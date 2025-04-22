package com.backendboard.domain.user.service;

import com.backendboard.domain.user.dto.UserReadResponse;

public interface UserService {
	UserReadResponse getUser(Long userId);
}
