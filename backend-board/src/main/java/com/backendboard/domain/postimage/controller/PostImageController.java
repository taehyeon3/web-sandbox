package com.backendboard.domain.postimage.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backendboard.domain.postimage.dto.PostImageCreateResponse;
import com.backendboard.domain.postimage.dto.PostImageReadResponse;
import com.backendboard.domain.postimage.dto.PostImageUpdateResponse;
import com.backendboard.domain.postimage.service.PostImageService;
import com.backendboard.global.error.dto.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "게시글 이미지", description = "게시글 이미지 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/post-images")
public class PostImageController {
	private final PostImageService postImageService;

	@Operation(
		summary = "이미지 생성 API",
		description = "새로운 이미지를 생성합니다.",
		security = {@SecurityRequirement(name = "bearerAuth")}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "201 성공",
			content = @Content(
				mediaType = "application/json", schema = @Schema(implementation = PostImageCreateResponse.class))),
		@ApiResponse(responseCode = "400", description = "이미지 파일이 아닙니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
	})
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<PostImageCreateResponse> createImage(@RequestParam("image") MultipartFile image)
		throws IOException {
		PostImageCreateResponse response = postImageService.uploadImage(image);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Operation(
		summary = "이미지 보기 API",
		description = "이미지를 보여줍니다.",
		security = {@SecurityRequirement(name = "bearerAuth")}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "200 성공",
			content = @Content(
				mediaType = "application/json", schema = @Schema(implementation = PostImageReadResponse.class))),
		@ApiResponse(responseCode = "400", description = "이미지 파일이 아닙니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
	})
	@GetMapping("/{imageId}")
	public ResponseEntity<PostImageReadResponse> readImage(@PathVariable Long imageId) {
		PostImageReadResponse response = postImageService.getImage(imageId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Operation(
		summary = "이미지 수정 API",
		description = "이미지를 수정합니다.",
		security = {@SecurityRequirement(name = "bearerAuth")}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "200 성공",
			content = @Content(
				mediaType = "application/json", schema = @Schema(implementation = PostImageUpdateResponse.class))),
		@ApiResponse(responseCode = "400", description = "이미지 파일이 아닙니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "이미지를 찾을 수 없습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
	})
	@PutMapping(value = "/{imageId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<PostImageUpdateResponse> updateImage(@RequestParam("image") MultipartFile image,
		@PathVariable Long imageId) throws IOException {
		PostImageUpdateResponse response = postImageService.updateImage(imageId, image);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Operation(
		summary = "이미지 삭제 API",
		description = "이미지를 삭제합니다.",
		security = {@SecurityRequirement(name = "bearerAuth")}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "204 성공",
			content = @Content(
				mediaType = "application/json", schema = @Schema())),
		@ApiResponse(responseCode = "404", description = "이미지를 찾을 수 없습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
	})
	@DeleteMapping("/{imageId}")
	public ResponseEntity<Void> deleteImage(@PathVariable Long imageId) {
		postImageService.deleteImage(imageId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
