package com.backendboard.domain.post.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "게시글 수정 요청 DTO")
@Getter
public class PostUpdateRequest {
	@Schema(description = "게시글 제목", example = "제목입니다.")
	@NotBlank
	@Size(max = 100)
	private final String title;

	@Schema(description = "게시글 내용", example = "내용입니다.")
	@NotBlank
	private final String content;

	@Schema(description = "이미지 아이디", example = "[1, 2, 3]")
	@NotNull
	private final List<Long> imageIds;

	@Builder
	private PostUpdateRequest(String title, String content, List<Long> imageIds) {
		this.title = title;
		this.content = content;
		this.imageIds = imageIds;
	}
}
