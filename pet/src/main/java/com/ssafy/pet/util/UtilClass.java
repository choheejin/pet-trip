package com.ssafy.pet.util;

import com.ssafy.pet.config.PaginationConstants;
import com.ssafy.pet.exception.ApplicationException;
import com.ssafy.pet.exception.errorcode.SearchErrorCode;

public class UtilClass {

	private UtilClass() {
	}

	public static int caculateOffest(int page) {
		if (page < 1) {
			throw new ApplicationException(SearchErrorCode.KEYWORD_MISSING, "페이지는 1보다 커야합니다.");
		}

		return (page - 1) * PaginationConstants.PAGE_SIZE;
	}

	public static int calculateTotalPages(int totalItems) {
		return (int) Math.ceil((double) totalItems / PaginationConstants.PAGE_SIZE);
	}
}
