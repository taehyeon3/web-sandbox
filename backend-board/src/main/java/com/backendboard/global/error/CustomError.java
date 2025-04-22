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
	AUTH_EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AU103", "토큰이 만료되었습니다."),
	AUTH_INVALID_TOKEN_FORM(HttpStatus.UNAUTHORIZED, "AU104", "토큰폼이 맞지않습니다.."),
	AUTH_INVALID_USER(HttpStatus.FORBIDDEN, "AU105", "본인이 아닙니다."),

	//유저 에러
	USER_DUPLICATION_ID(HttpStatus.FORBIDDEN, "UR100", "중복된 아이디 입니다."),
	USER_DUPLICATION_NICKNAME(HttpStatus.FORBIDDEN, "UR101", "중복된 닉네임 입니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "UR102", "유저를 찾을 수 없습니다."),

	//댓글 에러
	COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "CM100", "댓글을 찾을 수 없습니다."),
	COMMENT_NOT_AUTHOR(HttpStatus.FORBIDDEN, "CM101", "댓글 작성자가 아닙니다."),

	//이미지 에러
	IMAGE_INVALID_TYPE(HttpStatus.BAD_REQUEST, "IM100", "이미지 타입이 아닙니다."),
	IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "IM101", "이미지를 찾을 수 없습니다."),

	//게시글 에러
	POST_NOT_FOUND(HttpStatus.NOT_FOUND, "PT100", "게시글을 찾을 수 없습니다."),
	POST_NOT_AUTHOR(HttpStatus.FORBIDDEN, "PT101", "게시글 작성자가 아닙니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;
}
