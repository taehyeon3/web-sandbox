package com.backendboard.domain.image.dto;

import com.backendboard.domain.image.entity.Image;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "이미지 수정 응답 DTO")
@Getter
public class ImageUpdateResponse {
	@Schema(description = "이미지 아이디", example = "1")
	private final Long id;

	@Schema(description = "이미지 원본 파일이름", example = "potato.png")
	private final String fileName;

	@Builder
	private ImageUpdateResponse(Long id, String fileName) {
		this.id = id;
		this.fileName = fileName;
	}

	public static ImageUpdateResponse toDto(Image image) {
		return ImageUpdateResponse.builder()
			.id(image.getId())
			.fileName(image.getOriginalFileName())
			.build();
	}
}
