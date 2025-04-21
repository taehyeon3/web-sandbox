package com.backendboard.domain.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "댓글수정 요청 DTO")
@Getter
public class CommentUpdateRequest {
	@Schema(description = "댓글 내용", example = "안녕하세요.")
	@NotBlank
	@Size(max = 1000)
	private final String content;

	@Builder
	private CommentUpdateRequest(String content) {
		this.content = content;
	}
}
