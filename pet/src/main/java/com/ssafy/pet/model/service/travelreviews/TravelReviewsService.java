package com.ssafy.pet.model.service.travelreviews;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.ssafy.pet.dto.ReviewImagesDto;
import com.ssafy.pet.dto.TravelReviewsDto;

public interface TravelReviewsService {
	// 리뷰 저장 
	int saveReview(TravelReviewsDto reviewDto);
	
	// 리뷰 이미지 저장
	Optional<Integer> saveReviewImage(ReviewImagesDto imageDto);
    
    // 리뷰 조회 
    List<Map<String, Object>> getReviewsWithThumbnail(Integer dog_size, String orderBy);
    
    // 리뷰 상세조회 = 이미지 전부 가져오기
    List<String> getReviewImages(int review_id);
    
	// 리뷰 id로 상세 조회
	TravelReviewsDto getReviewDetail(int id);
	
	// 좋아요 추가
    void addFavorite(String userId, int reviewId);

    // 좋아요 취소
    void removeFavorite(String userId, int reviewId);

    // 좋아요 확인
    boolean checkLiked(int reviewId, String userId);
}
