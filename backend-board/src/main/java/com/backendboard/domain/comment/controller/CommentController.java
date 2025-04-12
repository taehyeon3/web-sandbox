package com.backendboard.domain.comment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backendboard.domain.comment.dto.CommentCreateRequest;
import com.backendboard.domain.comment.dto.CommentCreateResponse;
import com.backendboard.domain.comment.service.CommentService;
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
				mediaType = "application/json", schema = @Schema(implementation = CommentCreateResponse.class)
			)),
	})
	@PostMapping
	public ResponseEntity<CommentCreateResponse> createComments(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestBody @Valid CommentCreateRequest request) {
		System.out.println("request = " + request.getContent());
		CommentCreateResponse response = commentService.createComment(request, customUserDetails.getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
