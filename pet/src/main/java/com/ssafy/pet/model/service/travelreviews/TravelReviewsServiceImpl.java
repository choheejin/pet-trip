package com.ssafy.pet.model.service.travelreviews;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ssafy.pet.dto.ReviewImagesDto;
import com.ssafy.pet.dto.TravelReviewsDto;
import com.ssafy.pet.model.mapper.TravelReviewsMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelReviewsServiceImpl implements TravelReviewsService {
	private final TravelReviewsMapper travelReviewsMapper;
	
	public int saveReview(TravelReviewsDto reviewDto) {
		travelReviewsMapper.saveReview(reviewDto);
        return reviewDto.getId();
	}
	
	
	@Override
    public Optional<Integer> saveReviewImage(ReviewImagesDto imageDto) {
        int cnt = travelReviewsMapper.saveReviewImage(imageDto);
        return cnt != 0 ? Optional.of(cnt) : Optional.empty();
    }

	@Override
	public List<TravelReviewsDto> getReviews(Integer dog_size, String orderBy) {
		return travelReviewsMapper.getReviews(orderBy, dog_size);
	}

	@Override
	public TravelReviewsDto getReviewDetails(int reviewId) {
		return travelReviewsMapper.getReviewDetails(reviewId);
	}
	
	@Override
    public List<ReviewImagesDto> getReviewDetailImages(int reviewId) {
        return travelReviewsMapper.getReviewDetailImages(reviewId);  // 이미지 목록 반환
    }

	@Override
	public Optional<Integer> updateReview(TravelReviewsDto reviewDto) {
		int result = travelReviewsMapper.updateReview(reviewDto);
		return result > 0 ? Optional.of(result) : Optional.empty();
	}

	@Override
	public Optional<Integer> deleteReview(int reviewId) {
		int result = travelReviewsMapper.deleteReview(reviewId);
		return result > 0 ? Optional.of(result) : Optional.empty();
	}
	
}
