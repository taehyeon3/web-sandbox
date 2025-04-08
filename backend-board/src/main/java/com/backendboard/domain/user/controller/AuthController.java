package com.backendboard.domain.user.controller;

import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.backendboard.domain.user.dto.JoinRequest;
import com.backendboard.domain.user.dto.JoinResponse;
import com.backendboard.domain.user.service.AuthService;
import com.backendboard.global.error.CustomError;
import com.backendboard.global.error.CustomException;
import com.backendboard.global.error.dto.ErrorResponse;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "인증", description = "인증 관련 API")
@RestController
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

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
		JoinResponse response = authService.joinProcess(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/reissue")
	public ResponseEntity<Void> reissue(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = extractedRefreshToken(request);
		String accessToken = authService.reissueProcess(refreshToken);
		response.setHeader("Authorization", "Bearer " + accessToken);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private static String extractedRefreshToken(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}
		return Arrays.stream(cookies)
			.filter(cookie -> cookie.getName().equals("refresh"))
			.map(Cookie::getValue)
			.findFirst()
			.orElseThrow(() -> new CustomException(CustomError.AUTH_NOT_FOUND_COOKIE));
	}
}
