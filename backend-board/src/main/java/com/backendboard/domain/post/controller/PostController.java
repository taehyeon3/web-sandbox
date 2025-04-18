package com.backendboard.domain.post.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.backendboard.domain.post.dto.PostCreateRequest;
import com.backendboard.domain.post.dto.PostCreateResponse;
import com.backendboard.domain.post.dto.PostReadResponse;
import com.backendboard.domain.post.dto.PostUpdateRequest;
import com.backendboard.domain.post.dto.PostUpdateResponse;
import com.backendboard.domain.post.service.PostService;
import com.backendboard.global.error.dto.ErrorResponse;
import com.backendboard.global.security.dto.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "게시판", description = "게시판 관련 API")
@RestController
@RequiredArgsConstructor
public class PostController {
	private final PostService postService;

	@Operation(
		summary = "게시글 생성 API",
		description = "새로운 게시글을 생성합니다.",
		security = {@SecurityRequirement(name = "bearerAuth")}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "201 성공",
			content = @Content(
				mediaType = "application/json", schema = @Schema(implementation = PostCreateResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
	})
	@PostMapping
	public ResponseEntity<PostCreateResponse> createPost(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestBody @Valid PostCreateRequest request) {
		PostCreateResponse response = postService.createPost(request, customUserDetails.getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Operation(
		summary = "게시긇 수정 API",
		description = "게시글을 수정합니다.",
		security = {@SecurityRequirement(name = "bearerAuth")}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "200 성공",
			content = @Content(
				mediaType = "application/json", schema = @Schema(implementation = PostUpdateResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "403", description = "작성자가 아닙니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
	})
	@PutMapping("/{postId}")
	public ResponseEntity<PostUpdateResponse> updatePost(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestBody @Valid PostUpdateRequest request, @PathVariable Long postId) {
		PostUpdateResponse response = postService.updatePost(request, postId, customUserDetails.getId());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Operation(
		summary = "게시글 보기 API",
		description = "게시글을 보여줍니다.",
		security = {@SecurityRequirement(name = "bearerAuth")}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "200 성공",
			content = @Content(
				mediaType = "application/json", schema = @Schema(implementation = PostReadResponse.class))),
		@ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
	})
	@GetMapping("/{postId}")
	public ResponseEntity<PostReadResponse> readPost(@PathVariable Long postId) {
		PostReadResponse response = postService.getPost(postId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Operation(
		summary = "게시글 삭제 API",
		description = "게시글을 삭제합니다.",
		security = {@SecurityRequirement(name = "bearerAuth")}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "204 성공", content = @Content()),
		@ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
	})
	@DeleteMapping("/{postId}")
	public ResponseEntity<Void> deletePost(
		@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long postId) {
		postService.deletePost(postId, customUserDetails.getId());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
