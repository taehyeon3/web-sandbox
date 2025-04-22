package com.backendboard.domain.user.dto;

import com.backendboard.domain.user.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "유저 닉네임 변경 응답 DTO")
@Getter
public class UserNicknameUpdateResponse {
	@Schema(description = "유저 닉네임", example = "감자")
	private final String nickname;

	@Builder
	private UserNicknameUpdateResponse(String nickname) {
		this.nickname = nickname;
	}

	public static UserNicknameUpdateResponse toDto(User user) {
		return builder()
			.nickname(user.getNickname())
			.build();
	}
}
