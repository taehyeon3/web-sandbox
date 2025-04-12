package com.backendboard.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "회원가입 요청 DTO")
@Getter
public class JoinRequest {

	@Schema(description = "로그인 아이디", example = "user")
	@NotBlank
	@Size(max = 30)
	private final String loginId;

	@Schema(description = "로그인 비밀번호", example = "1234")
	@NotBlank
	@Size(max = 30)
	private final String password;

	@Schema(description = "유저의 이름", example = "감자")
	@NotBlank
	@Size(max = 30)
	private final String username;

	@Schema(description = "닉네임", example = "배고픈감자")
	@NotBlank
	@Size(max = 30)
	private final String nickname;

	@Builder
	private JoinRequest(String loginId, String password, String username, String nickname) {
		this.loginId = loginId;
		this.password = password;
		this.username = username;
		this.nickname = nickname;
	}
}
