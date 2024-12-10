package com.ssafy.pet.model.service.travelreviews;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ssafy.pet.dto.ReviewImagesDto;
import com.ssafy.pet.dto.TravelReviewsDto;
import com.ssafy.pet.model.mapper.TravelReviewsMapper;
import com.ssafy.pet.model.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelReviewsServiceImpl implements TravelReviewsService {
	private final TravelReviewsMapper travelReviewsMapper;
	private final UserMapper userMapper;
	
	public int saveReview(TravelReviewsDto reviewDto) {
		int user_id = userMapper.findIdByUserId(reviewDto.getUserId());
		
		reviewDto.setUser_id(user_id);
		
		travelReviewsMapper.saveReview(reviewDto);
        return reviewDto.getId();
	}
	
	
	@Override
    public Optional<Integer> saveReviewImage(ReviewImagesDto imageDto) {
        int cnt = travelReviewsMapper.saveReviewImage(imageDto);
        return cnt != 0 ? Optional.of(cnt) : Optional.empty();
    }


	@Override
	public List<Map<String, Object>> getReviewsWithThumbnail(Integer dog_size, String orderBy) {
	    return travelReviewsMapper.getReviewsWithThumbnail(dog_size, orderBy);
	}
	
	@Override
    public List<String> getReviewImages(int review_id) {
        // ReviewMapper의 getReviewImages 메서드를 호출하여 이미지 리스트 반환
        return travelReviewsMapper.getReviewImages(review_id);
    }

	@Override
    public TravelReviewsDto getReviewDetail(int id) {
		int cnt = travelReviewsMapper.incrementViewCount(id);
		System.out.println("view_cnt 를 1 증가 : "+cnt);
        return travelReviewsMapper.getReviewDetail(id);
    }
	
	@Override
    public void addFavorite(String user_id, int reviewId) {
		int userId = userMapper.findIdByUserId(user_id);

        travelReviewsMapper.insertFavorite(userId, reviewId);
    }

    @Override
    public void removeFavorite(String user_id, int reviewId) {
		int userId = userMapper.findIdByUserId(user_id);

        travelReviewsMapper.deleteFavorite(userId, reviewId);
    }
    
    @Override
    public boolean checkLiked(int reviewId, String user_id) {
		int userId = userMapper.findIdByUserId(user_id);

    	Map<String, Object> params = new HashMap<>();
        params.put("reviewId", reviewId);
        params.put("userId", userId);
        int count = travelReviewsMapper.checkLiked(params);
        return count > 0; // 결과를 boolean으로 변환
    }
    
    
	
}
