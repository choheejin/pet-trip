package com.ssafy.pet.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AttractionsDto {
	private int id;
	private int content_id;
	private String title;
	private int gugun_code;
	private String first_image1;
	private String first_image2;
	private int map_level;
	private float latitude;
	private float longitude;
	private String tel;
	private String addr1;
	private String addr2;
	private String homepage;
	private String overview;
	private int sido_code;
	private int content_type_id;
	private int favorite_cnt;
}
