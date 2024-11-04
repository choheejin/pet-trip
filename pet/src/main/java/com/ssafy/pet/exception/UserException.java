package com.ssafy.pet.exception;

public class UserException extends RuntimeException {
	private final UserExceptionType type;
	
	public UserException(UserExceptionType type) {
		super(type.getMessage());
		this.type = type;
	}

	public UserExceptionType getType() {
		return type;
	}
}
