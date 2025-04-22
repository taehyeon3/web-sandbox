package com.backendboard.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "유저 닉네임 변경 요청 DTO")
@Getter
public class UserNicknameUpdateRequest {
	@Schema(description = "유저 닉네임", example = "감자")
	@NotBlank
	@Size(max = 100)
	private final String nickname;

	@Builder
	private UserNicknameUpdateRequest(String nickname) {
		this.nickname = nickname;
	}
}
