package com.ssafy.pet.exception;

public class UserAlreadyExistsException extends RuntimeException{
	public UserAlreadyExistsException() {
		super("이미 존재하는 아이디 입니다.\n다시 입력해주세요");
	}
}
