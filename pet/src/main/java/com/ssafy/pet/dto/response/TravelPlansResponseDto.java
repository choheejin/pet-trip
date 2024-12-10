package com.ssafy.pet.dto.response;

import java.util.List;

import com.ssafy.pet.dto.TravelPlansDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TravelPlansResponseDto {
	private List<TravelPlansDto> plans;
	private List<Boolean> favoritePlans;
	private int total_pages;
}
