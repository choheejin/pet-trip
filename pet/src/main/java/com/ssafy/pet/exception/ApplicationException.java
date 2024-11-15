package com.ssafy.pet.exception;

import org.springframework.http.HttpStatus;

import com.ssafy.pet.exception.errorcode.ErrorCode;
import com.ssafy.pet.exception.errorcode.SearchErrorCode;

public class ApplicationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private final ErrorCode errorCode;
	
	public ApplicationException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
	
	public ApplicationException(ErrorCode errorCode, String keyword) {
		super(((SearchErrorCode) errorCode).GetFormattedMessage(keyword));
		this.errorCode = errorCode;
	}
	
	public HttpStatus getStatus() {
		return errorCode.getStatus();
	}
	
	public String getMessage() {
		return errorCode.getMessage();
	}
}
