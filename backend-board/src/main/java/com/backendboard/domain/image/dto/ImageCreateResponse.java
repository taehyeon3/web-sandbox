package com.backendboard.domain.image.dto;

import com.backendboard.domain.image.entity.Image;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "이미지 생성 응답 DTO")
@Getter
public class ImageCreateResponse {
	@Schema(description = "이미지 아이디", example = "1")
	private final Long id;

	@Schema(description = "이미지 원본 파일이름", example = "potato.png")
	private final String fileName;

	@Builder
	private ImageCreateResponse(Long id, String fileName) {
		this.id = id;
		this.fileName = fileName;
	}

	public static ImageCreateResponse toDto(Image image) {
		return ImageCreateResponse.builder()
			.id(image.getId())
			.fileName(image.getOriginalFileName())
			.build();
	}
}
