package com.ssafy.pet.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ProfileImageDto {
	private int id;
	private int user_id; // Users 테이블의 id 
	private String original_name;
	private String stored_name;
	private String file_path;
	private LocalDateTime uploaded_at; // 업로드 시간 
}
