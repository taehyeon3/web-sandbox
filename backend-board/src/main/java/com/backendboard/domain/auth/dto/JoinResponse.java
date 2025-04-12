package com.backendboard.domain.auth.dto;

import com.backendboard.domain.user.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "회원가입 응답 DTO")
@Getter
public class JoinResponse {
	@Schema(description = "회원 고유 번호", example = "1")
	private final Long id;

	@Schema(description = "로그인 아이디", example = "user")
	private final String loginId;

	@Schema(description = "유저의 이름", example = "감자")
	private final String username;

	@Schema(description = "닉네임", example = "배고픈감자")
	private final String nickname;

	@Builder
	private JoinResponse(Long id, String loginId, String username, String nickname) {
		this.id = id;
		this.loginId = loginId;
		this.username = username;
		this.nickname = nickname;
	}

	public static JoinResponse toDto(User user) {
		return builder()
			.id(user.getId())
			.loginId(user.getAuthUser().getUsername())
			.username(user.getUsername())
			.nickname(user.getNickname())
			.build();
	}
}
