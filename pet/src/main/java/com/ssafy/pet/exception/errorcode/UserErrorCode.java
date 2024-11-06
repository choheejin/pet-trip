package com.ssafy.pet.exception.errorcode;

import org.springframework.http.HttpStatus;

public enum UserErrorCode implements ErrorCode {
	USER_ALREADY_EXISTS("이미 존재하는 아이디 입니다.\n다시 입력해주세요", HttpStatus.CONFLICT),
	EXPIRED_JWT("로그인 시간이 만료 되었습니다.\n다시 로그인 하세요", HttpStatus.UNAUTHORIZED),
	UNAUTHORIZED("계정 권한이 유효하지 않습니다.\n다시 로그인을 하세요.", HttpStatus.UNAUTHORIZED);
	
	private final String message;
	private final HttpStatus status;

	private UserErrorCode(String message, HttpStatus status) {
		this.message = message;
		this.status = status;
	}

	@Override
	public HttpStatus getStatus() {
		return this.status;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

}
