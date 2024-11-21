package com.ssafy.pet.dto;

import java.util.List;

import com.ssafy.pet.config.PaginationConstants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserPlansResponseDto {

	private List<TravelPlansDto> plans;
	private boolean[] favoritePlans = new boolean[PaginationConstants.PAGE_SIZE];
	private int total_pages;
}
