package com.ssafy.pet.model.service.travelreviews;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.pet.dto.ReviewImagesDto;
import com.ssafy.pet.dto.TravelReviewsDto;

public interface TravelReviewsService {
	// 리뷰 저장 
	Optional<Integer> saveReview(TravelReviewsDto reviewDto, List<MultipartFile> files);
	// 리뷰 조회 
    List<TravelReviewsDto> getReviews(String orderBy, Integer dogSize);
    // 리뷰 상세조회 
    TravelReviewsDto getReviewDetails(int reviewId);
 // 이미지 상세 조회
    List<ReviewImagesDto> getReviewDetailImages(int reviewId);
    // 리뷰 수정하기 
    Optional<Integer> updateReview(TravelReviewsDto reviewDto);
    // 리뷰 삭제하기 
    Optional<Integer> deleteReview(int reviewId);
}
