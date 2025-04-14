package com.backendboard.domain.image.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "이미지 컬렉션 수정 요청 DTO")
@Getter
public class ImageCollectionUpdateRequest {
	@Schema(description = "이미지 아이디 리스트", example = "[1, 2, 3]")
	List<Integer> imageIds;
}
