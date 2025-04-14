package com.backendboard.domain.image.dto;

import java.util.List;

import com.backendboard.domain.image.entity.Image;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "이미지 컬렉션 생성 응답 DTO")
@Getter
public class ImageCollectionCreateResponse {
	@Schema(description = "이미지 컬렉션 아이디", example = "1")
	private final Long imageCollectionId;

	@Schema(description = "이미지 아이디 리스트", example = "[1, 2, 3]")
	private final List<Long> imageIds;

	@Builder
	private ImageCollectionCreateResponse(Long imageCollectionId, List<Long> imageIds) {
		this.imageCollectionId = imageCollectionId;
		this.imageIds = imageIds;
	}

	public static ImageCollectionCreateResponse toDto(Long imageCollectionId, List<Image> images) {
		return ImageCollectionCreateResponse.builder()
			.imageCollectionId(imageCollectionId)
			.imageIds(images.stream().map(Image::getId).toList())
			.build();
	}
}
