package com.backendboard.global.error.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.backendboard.global.error.CustomException;
import com.backendboard.global.error.dto.ErrorResponse;

@ControllerAdvice
public class CustomExceptionHandler {
	@ExceptionHandler(CustomException.class)
	protected ResponseEntity<ErrorResponse> handleCustomException(final CustomException exception) {
		return ErrorResponse.toResponseEntity(exception);
	}
}
