package com.ssafy.pet.dto;

import lombok.Data;

@Data
public class TravelPlanItemsDto {
	private int id;
	private String note;
	private int order;
	private int plan_id;
	private int content_id;
}
