package com.ssafy.pet.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TravelReviewsDto {
    private int id;
    private int plan_id;
    private int user_id;
    private String title;
    private String content;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private boolean is_public;
    private int view_cnt;
    private int favorite_cnt;
    private String thumbnail_image;
    private byte dog_size;
}

