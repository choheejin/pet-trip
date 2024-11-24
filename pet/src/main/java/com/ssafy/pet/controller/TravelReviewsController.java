package com.ssafy.pet.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.pet.dto.ReviewImagesDto;
import com.ssafy.pet.dto.TravelReviewsDto;
import com.ssafy.pet.model.service.travelreviews.TravelReviewsService;
import com.ssafy.pet.model.service.user.UserHelperService;
import com.ssafy.pet.util.JWTUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
@Slf4j
public class TravelReviewsController {
	private final JWTUtil jwtUtil;
	private final TravelReviewsService travelReviewsService;
	private final UserHelperService userHelperService;
	
	// 리뷰 저장
	@PostMapping
	public ResponseEntity<?> saveReview(@RequestHeader("accessToken") String header,
	                                    @RequestPart("review") TravelReviewsDto reviewDto,
	                                    @RequestPart(value = "files", required = false) List<MultipartFile> files) {
	    try {
	        // accessToken에서 user_id 추출
	    	int user_id = userHelperService.getUserIdFromHeader(header);
	        
	        // reviewDto에 user_id 설정
	        reviewDto.setUser_id(user_id);

	        // 서비스 호출
	        Optional<Integer> reviewId = travelReviewsService.saveReview(reviewDto, files);

	        // 응답 처리
	        return reviewId.isPresent()
	                ? ResponseEntity.status(HttpStatus.CREATED).body(reviewId.get())
	                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Review saving failed");
	    } catch (Exception e) {
	        log.error("Error saving review: {}", e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
	    }
	}

	
	// 리뷰 전체 조회
	@GetMapping
    public ResponseEntity<?> getReviews(@RequestParam(defaultValue = "date") String orderBy,
                                        @RequestParam(required = false) Integer dog_size) {
        try {
            List<TravelReviewsDto> reviews = travelReviewsService.getReviews(orderBy, dog_size);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            log.error("Error fetching reviews: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
	
	// 리뷰 상세 조회
	@GetMapping("/{review_id}")
    public ResponseEntity<?> getReviewDetails(@PathVariable int review_id) {
        try {
            TravelReviewsDto reviewDetails = travelReviewsService.getReviewDetails(review_id);
            return ResponseEntity.ok(reviewDetails);
        } catch (Exception e) {
            log.error("Error fetching review details: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
	
	// 리뷰 상세 조회 - 이미지 가져오기 
	@GetMapping("/{review_id}/images")
    public ResponseEntity<?> getReviewDetailImages(@PathVariable int review_id) {
        try {
            List<ReviewImagesDto> images = travelReviewsService.getReviewDetailImages(review_id);
            return ResponseEntity.ok(images);
        } catch (Exception e) {
            log.error("Error fetching review images: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
	
	// 리뷰 수정
    @PutMapping("/{review_id}")
    public ResponseEntity<?> updateReview(@PathVariable int review_id,
                                          @RequestBody TravelReviewsDto reviewDto) {
        try {
            reviewDto.setId(review_id);
            Optional<Integer> updatedId = travelReviewsService.updateReview(reviewDto);
            return updatedId.isPresent()
                    ? ResponseEntity.ok("Review updated successfully")
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Review update failed");
        } catch (Exception e) {
            log.error("Error updating review: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable int reviewId) {
        try {
            Optional<Integer> deletedId = travelReviewsService.deleteReview(reviewId);
            return deletedId.isPresent()
                    ? ResponseEntity.ok("Review deleted successfully")
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Review deletion failed");
        } catch (Exception e) {
            log.error("Error deleting review: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

}
