package com.backendboard.domain.postimage.dto;

import com.backendboard.domain.postimage.entity.PostImage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "이미지 수정 응답 DTO")
@Getter
public class PostImageUpdateResponse {
	@Schema(description = "이미지 아이디", example = "1")
	private final Long id;

	@Schema(description = "이미지 원본 파일이름", example = "potato.png")
	private final String fileName;

	@Schema(description = "이미지 url", example = "1_potato.png")
	private final String fileUrl;

	@Builder
	private PostImageUpdateResponse(Long id, String fileName, String fileUrl) {
		this.id = id;
		this.fileName = fileName;
		this.fileUrl = fileUrl;
	}

	public static PostImageUpdateResponse toDto(PostImage postImage) {
		return PostImageUpdateResponse.builder()
			.id(postImage.getId())
			.fileName(postImage.getOriginalFileName())
			.fileUrl(postImage.getStoredFileName())
			.build();
	}
}
