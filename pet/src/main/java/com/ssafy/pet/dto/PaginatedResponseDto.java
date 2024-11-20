package com.ssafy.pet.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginatedResponseDto<T> {
	private List<T> data;
	private int totalPage;
	
	public PaginatedResponseDto(List<T> data, int totalPage)
	{
		this.data = data;
		this.totalPage = totalPage;
	}
}