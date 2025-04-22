package com.backendboard.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "비밀번호 변경 요청 DTO")
@Getter
public class PasswordUpdateRequest {
	@Schema(description = "비밀번호", example = "1234")
	@NotBlank
	@Size(max = 30)
	private final String password;

	@Builder
	private PasswordUpdateRequest(String password) {
		this.password = password;
	}
}
