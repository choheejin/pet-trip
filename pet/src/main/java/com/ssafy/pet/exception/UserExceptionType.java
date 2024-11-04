package com.ssafy.pet.exception;

import org.springframework.http.HttpStatus;

public enum UserExceptionType {
	ACCESS_DENIED("요청 처리 중 문제가 발생하였습니다", HttpStatus.INTERNAL_SERVER_ERROR),
	USER_ALREADY_EXISTS("이미 존재하는 아이디 입니다.\\n다시 입력해주세요", HttpStatus.CONFLICT),
	UN_AUTHORIZED("계정 권한이 유효하지 않습니다.\\n다시 로그인을 하세요.", HttpStatus.UNAUTHORIZED);

	private final String message;
	private final HttpStatus status;
	
	private UserExceptionType(String message, HttpStatus status) {
		this.message = message;
		this.status = status;
	}
	
	public String getMessage() {
		return message;
	}
	
	public HttpStatus getStatus() {
		return status;
	}
}
