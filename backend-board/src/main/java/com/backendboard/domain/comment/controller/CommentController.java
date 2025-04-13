package com.backendboard.domain.comment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backendboard.domain.comment.dto.CommentCreateRequest;
import com.backendboard.domain.comment.dto.CommentCreateResponse;
import com.backendboard.domain.comment.dto.CommentReadResponse;
import com.backendboard.domain.comment.dto.CommentUpdateRequest;
import com.backendboard.domain.comment.dto.CommentUpdateResponse;
import com.backendboard.domain.comment.service.CommentService;
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

@Tag(name = "댓글", description = "댓글 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/comments")
public class CommentController {
	private final CommentService commentService;

	@Operation(
		summary = "댓글 생성 API",
		description = "새로운 댓글을 생성합니다.",
		security = {@SecurityRequirement(name = "bearerAuth")}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "201 성공",
			content = @Content(
				mediaType = "application/json", schema = @Schema(implementation = CommentCreateResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
	})
	@PostMapping
	public ResponseEntity<CommentCreateResponse> createComments(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestBody @Valid CommentCreateRequest request) {
		CommentCreateResponse response = commentService.createComment(request, customUserDetails.getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Operation(
		summary = "댓글 수정 API",
		description = "댓글을 수정합니다.",
		security = {@SecurityRequirement(name = "bearerAuth")}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "200 성공",
			content = @Content(
				mediaType = "application/json", schema = @Schema(implementation = CommentUpdateResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "403", description = "작성자가 아닙니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
	})
	@PutMapping("/{commentId}")
	public ResponseEntity<CommentUpdateResponse> updateComment(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestBody @Valid CommentUpdateRequest request, @PathVariable Long commentId) {
		CommentUpdateResponse response = commentService.updateComment(request, commentId, customUserDetails.getId());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Operation(
		summary = "댓글 보기 API",
		description = "댓글을 봅니다.",
		security = {@SecurityRequirement(name = "bearerAuth")}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "200 성공",
			content = @Content(
				mediaType = "application/json", schema = @Schema(implementation = CommentReadResponse.class))),
		@ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
	})
	@GetMapping("/{commentId}")
	public ResponseEntity<CommentReadResponse> readComment(@PathVariable Long commentId) {
		CommentReadResponse response = commentService.getComment(commentId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Operation(
		summary = "댓글 삭제 API",
		description = "댓글을 삭제합니다.",
		security = {@SecurityRequirement(name = "bearerAuth")}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "204 성공", content = @Content()),
		@ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
	})
	@DeleteMapping("/{commentId}")
	public ResponseEntity<Void> deleteComment(
		@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long commentId) {
		commentService.deleteComment(commentId, customUserDetails.getId());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
