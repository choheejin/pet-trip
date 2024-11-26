package com.ssafy.pet.model.mapper;

import java.util.List;
import java.util.Map;

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

	// 리뷰 조회 - 썸네일 같이 가져오
	List<Map<String, Object>> getReviewsWithThumbnail(@Param("dog_size") Integer dog_size,
			@Param("orderBy") String orderBy);

	// 리뷰 상세조회 = 이미지 전부 가져오기
	List<String> getReviewImages(int review_id);

	// 리뷰 id로 상세 조회
	TravelReviewsDto getReviewDetail(int id);

	// view_cnt 증가
	int incrementViewCount(int id);

	// 좋아요 추가
    void insertFavorite(@Param("userId") int userId, @Param("reviewId") int reviewId);

    // 좋아요 취소
    void deleteFavorite(@Param("userId") int userId, @Param("reviewId") int reviewId);
    
    // 좋아요 확인
    int checkLiked(Map<String, Object> params);

}
