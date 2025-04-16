package com.backendboard.domain.postlike.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backendboard.domain.postlike.dto.PostLikeCountResponse;
import com.backendboard.domain.postlike.dto.PostLikeStatusResponse;
import com.backendboard.domain.postlike.service.PostLikeService;
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
@RequestMapping("/post-likes")
public class PostLikeController {
	private final PostLikeService postLikeService;

	@Operation(
		summary = "게시판 좋아요 토글 API",
		description = "게시판 좋아요 토글 기능입니다.",
		security = {@SecurityRequirement(name = "bearerAuth")}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "200 성공",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = PostLikeStatusResponse.class)))
	})
	@PostMapping("/{postId}")
	public ResponseEntity<PostLikeStatusResponse> toggleLike(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@PathVariable Long postId) {
		PostLikeStatusResponse response = postLikeService.toggleLike(customUserDetails.getId(), postId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Operation(
		summary = "게시판 좋아요 보기 API",
		description = "게시판 좋아요 보기 기능입니다.",
		security = {@SecurityRequirement(name = "bearerAuth")}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "200 성공",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = PostLikeStatusResponse.class)))
	})
	@GetMapping("/{postId}/status")
	public ResponseEntity<PostLikeStatusResponse> readLikeStatus(
		@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long postId) {
		PostLikeStatusResponse response = postLikeService.getLikeStatus(customUserDetails.getId(), postId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Operation(
		summary = "게시판 좋아요 수 API",
		description = "게시판 좋아요 수를 보는 기능입니다.",
		security = {@SecurityRequirement(name = "bearerAuth")}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "200 성공",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = PostLikeCountResponse.class)))
	})
	@GetMapping("/{postId}/count")
	public ResponseEntity<PostLikeCountResponse> getLikeCount(@PathVariable Long postId) {
		PostLikeCountResponse response = postLikeService.getLikeCount(postId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
