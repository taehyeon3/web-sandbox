package com.backendboard.domain.like.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backendboard.domain.like.dto.LikeResponse;
import com.backendboard.domain.like.service.LikeService;
import com.backendboard.global.security.dto.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "좋아요", description = "좋아요 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikeController {
	private final LikeService likeService;

	@Operation(
		summary = "게시판 좋아요 토글 API",
		description = "게시판 좋아요 토글 기능입니다.",
		security = {@SecurityRequirement(name = "bearerAuth")}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "200 성공",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = LikeResponse.class)))
	})
	@PostMapping("/posts/{postId}")
	public ResponseEntity<LikeResponse> togglePostLike(@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@PathVariable Long postId) {
		LikeResponse response = likeService.togglePostLike(customUserDetails.getId(), postId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
