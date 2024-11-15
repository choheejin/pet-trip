package com.ssafy.pet.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler({ ApplicationException.class })
	protected ResponseEntity handleCustomException(ApplicationException e) {
		e.printStackTrace();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("errorCode", e.getStatus());
		resultMap.put("message", e.getMessage());
		return new ResponseEntity(resultMap, e.getStatus());
	}

	@ExceptionHandler({ Exception.class })
	protected ResponseEntity handleServerException(Exception e) {
		e.printStackTrace();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("errorCode", HttpStatus.INTERNAL_SERVER_ERROR);
		resultMap.put("message", e.getMessage());
		return new ResponseEntity(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
