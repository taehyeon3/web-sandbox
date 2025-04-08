package com.backendboard.global.error;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomError {
	//인증 에러
	AUTH_NOT_FOUND_ID(HttpStatus.NOT_FOUND, "AU100", "존재하지 않는 아이디 입니다."),
	AUTH_NOT_FOUND_COOKIE(HttpStatus.UNAUTHORIZED, "AU101", "쿠키를 찾을 수 없습니다."),
	AUTH_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AU102", "토큰이 유효하지 않습니다."),

	//유저 에러
	USER_DUPLICATION_ID(HttpStatus.FORBIDDEN, "UR100", "중복된 아이디 입니다."),
	USER_DUPLICATION_NICKNAME(HttpStatus.FORBIDDEN, "UR101", "중복된 닉네임 입니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;
}
