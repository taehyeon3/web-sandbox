package com.backendboard.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "게시글 수정 요청 DTO")
@Getter
public class PostUpdateRequest {
	@Schema(description = "게시글 제목", example = "제목입니다.")
	@NotBlank
	private final String title;

	@Schema(description = "게시글 내용", example = "내용입니다.")
	@NotBlank
	private final String content;

	@Builder
	private PostUpdateRequest(String title, String content) {
		this.title = title;
		this.content = content;
	}
}
