package com.ssafy.pet.dto;

import lombok.Data;

@Data
public class TravelPlansDto {
	private int id;
	private String title;
	private String created_at;
	private String end_at;
	private boolean is_public;
	private String description;
	private int user_id;
	private int view_cnt;
}