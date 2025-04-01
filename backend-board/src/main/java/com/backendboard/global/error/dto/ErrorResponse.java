package com.backendboard.global.error.dto;

import org.springframework.http.ResponseEntity;

import com.backendboard.global.error.CustomException;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "에러 응답 DTO")
public record ErrorResponse(@Schema(description = "에러 메시지", example = "에러가 발생했습니다.") String message) {
	public static ResponseEntity<ErrorResponse> toResponseEntity(final CustomException exception) {
		return ResponseEntity.status(exception.getError().getStatus())
			.body(new ErrorResponse(exception.getError().getMessage()));
	}
}
