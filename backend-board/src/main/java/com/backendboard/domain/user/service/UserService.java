package com.backendboard.domain.user.service;

import com.backendboard.domain.user.dto.UserNicknameUpdateRequest;
import com.backendboard.domain.user.dto.UserNicknameUpdateResponse;
import com.backendboard.domain.user.dto.UserReadResponse;

public interface UserService {
	UserReadResponse getUser(Long userId);

	UserNicknameUpdateResponse updateNickname(UserNicknameUpdateRequest request, Long userId, Long authUserId);
}
