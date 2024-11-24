package com.ssafy.pet.dto;

import lombok.Data;

@Data
public class ReviewsFavoritesDto {
	private int id;
    private int review_id;
    private int user_id;
}
