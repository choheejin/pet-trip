package com.ssafy.pet.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReviewImagesDto {
	private int id;
    private int review_id;
    private String original_name;
    private String stored_name;
    private String file_path;
    private LocalDateTime uploaded_at;
    private boolean is_thumbnail;

}
