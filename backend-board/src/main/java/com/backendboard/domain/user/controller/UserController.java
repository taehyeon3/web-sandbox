package com.backendboard.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backendboard.domain.user.dto.UserNicknameUpdateRequest;
import com.backendboard.domain.user.dto.UserNicknameUpdateResponse;
import com.backendboard.domain.user.dto.UserReadResponse;
import com.backendboard.domain.user.service.UserService;
import com.backendboard.global.error.dto.ErrorResponse;
import com.backendboard.global.security.dto.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "유저", description = "유저 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	private final UserService userService;

	@Operation(
		summary = "유저 보기 API",
		description = "유저의 정보를 보여줍니다.",
		security = {@SecurityRequirement(name = "bearerAuth")}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "200 성공",
			content = @Content(
				mediaType = "application/json", schema = @Schema(implementation = UserReadResponse.class))),
		@ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
	})
	@GetMapping("/{userId}")
	public ResponseEntity<UserReadResponse> readUser(@PathVariable Long userId) {
		UserReadResponse response = userService.getUser(userId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Operation(
		summary = "유저 닉네임 변경 API",
		description = "유저의 닉네임을 변경합니다.",
		security = {@SecurityRequirement(name = "bearerAuth")}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "200 성공",
			content = @Content(
				mediaType = "application/json", schema = @Schema(implementation = UserNicknameUpdateResponse.class))),
		@ApiResponse(responseCode = "403", description = "중복된 닉네임입니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
	})
	@PatchMapping("/{userId}")
	public ResponseEntity<UserNicknameUpdateResponse> updateNickname(@PathVariable Long userId,
		@AuthenticationPrincipal CustomUserDetails customUserDetails, UserNicknameUpdateRequest request) {
		UserNicknameUpdateResponse response = userService.updateNickname(request, userId, customUserDetails.getId());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
