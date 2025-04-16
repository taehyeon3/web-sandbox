package com.backendboard.domain.like.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "좋아요 응답 DTO")
@Getter
public class LikeResponse {
	@Schema(description = "좋아요 상태", example = "true")
	private final boolean isLiked;

	@Builder
	private LikeResponse(boolean isLiked) {
		this.isLiked = isLiked;
	}

	public static LikeResponse toDto(boolean isLiked) {
		return LikeResponse.builder()
			.isLiked(isLiked)
			.build();
	}
}
