package com.backendboard.domain.image.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backendboard.domain.image.dto.ImageCollectionCreateRequest;
import com.backendboard.domain.image.dto.ImageCollectionCreateResponse;
import com.backendboard.domain.image.dto.ImageCollectionReadResponse;
import com.backendboard.domain.image.dto.ImageCollectionUpdateRequest;
import com.backendboard.domain.image.dto.ImageCollectionUpdateResponse;
import com.backendboard.domain.image.service.ImageCollectionService;
import com.backendboard.global.error.dto.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "이미지 컬렉션", description = "이미지 컬렉션 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/image-collections")
public class ImageCollectionController {
	private final ImageCollectionService imageCollectionService;

	@Operation(
		summary = "이미지 컬렉션 생성 API",
		description = "새로운 이미지 컬렉션을 생성합니다.",
		security = {@SecurityRequirement(name = "bearerAuth")}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "201 성공",
			content = @Content(
				mediaType = "application/json", schema = @Schema(implementation = ImageCollectionCreateResponse.class))),
		@ApiResponse(responseCode = "404", description = "이미지를 찾을 수 없습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
	})
	@PostMapping
	public ResponseEntity<ImageCollectionCreateResponse> createImageCollection(
		@RequestBody ImageCollectionCreateRequest request) {
		ImageCollectionCreateResponse response = imageCollectionService.createImageCollection(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Operation(
		summary = "이미지 컬렉션 보기 API",
		description = "이미지 컬렉션을 보여줍니다.",
		security = {@SecurityRequirement(name = "bearerAuth")}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "200 성공",
			content = @Content(
				mediaType = "application/json", schema = @Schema(implementation = ImageCollectionReadResponse.class))),
		@ApiResponse(responseCode = "404", description = "이미지 컬렉션을 찾을 수 없습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
	})
	@GetMapping("/{imageCollectionId}")
	public ResponseEntity<ImageCollectionReadResponse> readImageCollection(@PathVariable Long imageCollectionId) {
		ImageCollectionReadResponse response = imageCollectionService.getImageCollection(imageCollectionId);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Operation(
		summary = "이미지 컬렉션 수정 API",
		description = "이미지 컬렉션을 수정합니다.",
		security = {@SecurityRequirement(name = "bearerAuth")}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "200 성공",
			content = @Content(
				mediaType = "application/json", schema = @Schema(implementation = ImageCollectionUpdateResponse.class))),
		@ApiResponse(responseCode = "404", description = "이미지를 찾을 수 없습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
	})
	@PutMapping(value = "/{imageCollectionId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ImageCollectionUpdateResponse> updateImageCollection(
		@RequestBody ImageCollectionUpdateRequest request, @PathVariable Long imageCollectionId) {
		ImageCollectionUpdateResponse response = imageCollectionService.updateImageCollection(request,
			imageCollectionId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Operation(
		summary = "이미지 컬렉션 삭제 API",
		description = "이미지 컬렉션을 삭제합니다.",
		security = {@SecurityRequirement(name = "bearerAuth")}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "204 성공",
			content = @Content(
				mediaType = "application/json", schema = @Schema())),
		@ApiResponse(responseCode = "404", description = "이미지 컬렉션을 찾을 수 없습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
	})
	@DeleteMapping("/{imageCollectionId}")
	public ResponseEntity<Void> deleteImageCollection(@PathVariable Long imageCollectionId) {
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
