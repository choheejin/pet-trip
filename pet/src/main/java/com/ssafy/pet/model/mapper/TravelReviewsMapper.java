package com.ssafy.pet.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.pet.dto.ReviewImagesDto;
import com.ssafy.pet.dto.TravelReviewsDto;

@Mapper
public interface TravelReviewsMapper {
	// 리뷰 저장
    int saveReview(TravelReviewsDto reviewDto);
    
    // 리뷰 이미지 저장
    int saveReviewImage(ReviewImagesDto imageDto);

    // 리뷰 조회 (정렬 및 크기 필터링)
    List<TravelReviewsDto> getReviews(@Param("orderBy") String orderBy,@Param("dog_size") Integer dog_size);

    // 리뷰 상세조회
    TravelReviewsDto getReviewDetails(int reviewId);
    
 // 이미지 상세 조회
    List<ReviewImagesDto> getReviewDetailImages(int reviewId);

    // 리뷰 수정
    int updateReview(TravelReviewsDto reviewDto);

    // 리뷰 삭제
    int deleteReview(int reviewId);

}
