package com.backendboard.domain.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.backendboard.domain.auth.dto.JoinRequest;
import com.backendboard.domain.auth.dto.JoinResponse;
import com.backendboard.domain.auth.dto.PasswordUpdateRequest;
import com.backendboard.domain.auth.dto.RefreshTokenDto;
import com.backendboard.domain.auth.service.AuthService;
import com.backendboard.global.error.CustomError;
import com.backendboard.global.error.CustomException;
import com.backendboard.global.error.dto.ErrorResponse;
import com.backendboard.global.security.dto.CustomUserDetails;
import com.backendboard.global.util.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "인증", description = "인증 관련 API")
@RestController
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	private final JwtUtil jwtUtil;

	@Operation(
		summary = "회원가입 API",
		description = "새로운 유저를 생성합니다.",
		security = {}
	)
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

	@Operation(
		summary = "비밀번호 변경 API",
		description = "비밀번호를 변경합니다.",
		security = {@SecurityRequirement(name = "bearerAuth")}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "204 성공", content = @Content()),
		@ApiResponse(responseCode = "403", description = "본인이 아닙니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
	})
	@PatchMapping("/users/password")
	public ResponseEntity<Void> updatePassword(PasswordUpdateRequest request,
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		authService.updatePassword(request, customUserDetails.getId());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@Operation(
		summary = "토큰 재발급 API - Swagger ui 에서 테스트 X",
		description = "새로운 토큰을 발급합니다.",
		security = {@SecurityRequirement(name = "bearerAuth")}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "200 성공",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = JoinResponse.class))),
		@ApiResponse(responseCode = "AU101", description = "401 쿠키를 찾을 수 없습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "AU102", description = "401 토큰이 유효하지 않습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "AU103", description = "401 토큰이 만료되었습니다..",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
	})
	@PostMapping("/reissue")
	public ResponseEntity<Void> reissue(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = jwtUtil.extractedRefreshToken(request.getCookies());

		validateRefreshTokenFormat(refreshToken);
		validateRefreshToken(refreshToken);
		validateTokenExpiration(refreshToken);

		String username = jwtUtil.getUsername(refreshToken);
		String role = jwtUtil.getRole(refreshToken);
		String accessToken = jwtUtil.createAccessToken(username, role);
		String newRefreshToken = jwtUtil.createRefreshToken(username, role);

		response.setHeader("Authorization", "Bearer " + accessToken);
		response.addCookie(jwtUtil.createRefreshCookie(newRefreshToken));
		RefreshTokenDto tokenDto = RefreshTokenDto.toDto(username, newRefreshToken);
		authService.deleteRefreshToken(refreshToken);
		authService.saveRefreshToken(tokenDto);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	public void validateTokenExpiration(String refreshToken) {
		if (jwtUtil.isExpired(refreshToken)) {
			throw new CustomException(CustomError.AUTH_EXPIRED_TOKEN);
		}
	}

	public void validateRefreshTokenFormat(String refreshToken) {
		if (refreshToken == null || !jwtUtil.getType(refreshToken).equals("refresh")) {
			throw new CustomException(CustomError.AUTH_INVALID_TOKEN_FORM);
		}
	}

	public void validateRefreshToken(String refreshToken) {
		if (!authService.isValidRefreshToken(refreshToken)) {
			throw new CustomException(CustomError.AUTH_INVALID_TOKEN);
		}
	}
}
