package com.ssafy.pet.exception.errorcode;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum TravelPlanErrorCode implements ErrorCode {
	UPDATE_FROBIDDEN("게시글에 대한 수정 권한이 없습니다", HttpStatus.FORBIDDEN),
	NO_CONTENT("게시글이 존재하지 않습니다", HttpStatus.NOT_FOUND);
	
	private final String message;
	private final HttpStatus status;


	private TravelPlanErrorCode(String messge, HttpStatus status) {
		this.message = messge;
		this.status = status;
	}
}
