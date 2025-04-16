package com.backendboard.domain.postlike.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "좋아요 수 응답 DTO")
@Getter
public class PostLikeCountResponse {
	@Schema(description = "좋아요 수", example = "123")
	private final Long count;

	@Builder
	private PostLikeCountResponse(Long count) {
		this.count = count;
	}

	public static PostLikeCountResponse toDto(Long count) {
		return PostLikeCountResponse.builder()
			.count(count)
			.build();
	}
}
