package com.backendboard.domain.image.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backendboard.domain.image.dto.ImageCreateResponse;
import com.backendboard.domain.image.service.ImageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "이미지", description = "이미지 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {
	private final ImageService imageService;

	@Operation(
		summary = "이미지 생성 API",
		description = "새로운 이미지를 생성합니다.",
		security = {@SecurityRequirement(name = "bearerAuth")}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "201 성공",
			content = @Content(
				mediaType = "application/json", schema = @Schema(implementation = ImageCreateResponse.class))),
	})
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ImageCreateResponse> createImage(@RequestParam("file") MultipartFile file)
		throws IOException {
		ImageCreateResponse response = imageService.uploadImage(file);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
