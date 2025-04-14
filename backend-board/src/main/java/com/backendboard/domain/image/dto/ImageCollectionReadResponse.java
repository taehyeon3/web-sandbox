package com.backendboard.domain.image.dto;

import java.util.List;

import com.backendboard.domain.image.entity.Image;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "이미지 컬렉션 보기 응답 DTO")
@Getter
public class ImageCollectionReadResponse {
	@Schema(description = "이미지 컬렉션 아이디", example = "1")
	private final Long imageCollectionId;

	@Schema(description = "이미지 아이디 리스트", example = "[1, 2, 3]")
	private final List<Long> imageIds;

	@Builder
	private ImageCollectionReadResponse(Long imageCollectionId, List<Long> imageIds) {
		this.imageCollectionId = imageCollectionId;
		this.imageIds = imageIds;
	}

	public static ImageCollectionReadResponse toDto(Long imageCollectionId, List<Image> images) {
		return ImageCollectionReadResponse.builder()
			.imageCollectionId(imageCollectionId)
			.imageIds(images.stream().map(Image::getId).toList())
			.build();
	}
}
