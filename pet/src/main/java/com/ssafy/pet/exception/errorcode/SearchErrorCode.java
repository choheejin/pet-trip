package com.ssafy.pet.exception.errorcode;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum SearchErrorCode implements ErrorCode {
	KEYWORD_MISSING("검색 키워드가 없습니다.", HttpStatus.BAD_REQUEST),
	NO_RESULTS_FOUND("검색 결과가 없습니다", HttpStatus.NOT_FOUND);
	
	private final String message;
	private final HttpStatus status;
	
	SearchErrorCode(String message, HttpStatus httpStatus)
	{
		this.message = message;
		this.status = httpStatus;
	}

	public String GetFormattedMessage(String keyword)
	{
		return String.format(this.message, keyword);
	}

}
