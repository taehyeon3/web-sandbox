package com.backendboard.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Schema(description = "게시글 보기 응답 DTO")
@Getter
public class PostReadResponse {
	@Schema(description = "게시글 제목", example = "제목입니다.")
	@NotBlank
	private String title;

	@Schema(description = "게시글 내용", example = "내용입니다.")
	@NotBlank
	private String content;
}
