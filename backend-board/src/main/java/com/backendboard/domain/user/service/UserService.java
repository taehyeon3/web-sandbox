package com.backendboard.domain.user.service;

import com.backendboard.domain.user.dto.UserInfoResponse;
import com.backendboard.domain.user.dto.UserNicknameUpdateRequest;
import com.backendboard.domain.user.dto.UserNicknameUpdateResponse;

public interface UserService {
	UserInfoResponse getInfo(Long authUserId);

	UserNicknameUpdateResponse updateNickname(UserNicknameUpdateRequest request, Long userId, Long authUserId);
}
