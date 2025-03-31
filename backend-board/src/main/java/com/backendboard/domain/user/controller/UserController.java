package com.backendboard.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.backendboard.domain.user.dto.JoinRequest;
import com.backendboard.domain.user.dto.JoinResponse;
import com.backendboard.domain.user.service.UserService;
import com.backendboard.global.error.dto.ErrorResponse;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "회원", description = "회원 관련 API")
@RestController
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "201 성공",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = JoinResponse.class))),
		@ApiResponse(responseCode = "UR100", description = "403 아이디가 중복입니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "UR101", description = "403 닉네임이 중복입니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
	})
	@PostMapping("/join")
	public ResponseEntity<JoinResponse> join(@RequestBody @Valid JoinRequest request) {
		JoinResponse response = userService.joinProcess(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
