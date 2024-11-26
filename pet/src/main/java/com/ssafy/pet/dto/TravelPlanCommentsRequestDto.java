package com.ssafy.pet.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class TravelPlanCommentsRequestDto {
	private int id;
	private int plan_id;
	private String user_id;
	private String comment;
	private LocalDateTime created_at;
	private LocalDateTime updated_at;
	private int parent_comment_id;
	private String metioned;
	private int level;
}
