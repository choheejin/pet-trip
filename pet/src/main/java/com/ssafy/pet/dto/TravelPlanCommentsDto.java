package com.ssafy.pet.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TravelPlanCommentsDto {
	private int id;
	private int plan_id;
	private int user_id;
	private String comment;
	private LocalDateTime created_at;
	private LocalDateTime updated_at;
	private int parent_comment_id;
	private Integer level;
}