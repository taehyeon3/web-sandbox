package com.backendboard.domain.postlike.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "좋아요 응답 DTO")
@Getter
public class PostLikeStatusResponse {
	@Schema(description = "좋아요 상태", example = "true")
	private final boolean isLiked;

	@Builder
	private PostLikeStatusResponse(boolean isLiked) {
		this.isLiked = isLiked;
	}

	public static PostLikeStatusResponse toDto(boolean isLiked) {
		return PostLikeStatusResponse.builder()
			.isLiked(isLiked)
			.build();
	}
}
