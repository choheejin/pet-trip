package com.ssafy.pet.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.pet.dto.ReviewImagesDto;
import com.ssafy.pet.dto.TravelReviewsDto;
import com.ssafy.pet.model.service.travelreviews.TravelReviewsService;
import com.ssafy.pet.model.service.user.UserHelperService;
import com.ssafy.pet.util.JWTUtil;

import io.jsonwebtoken.io.IOException;
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

	// 이미지 저장
	@PostMapping("/insertimage")
	public ResponseEntity<?> insertImage(@RequestHeader("accessToken") String header,
			@RequestParam(value="file") MultipartFile file,
			@RequestParam("is_thumbnail") boolean isThumbnail,
			@RequestParam("plan_id") int plan_id,
			@RequestParam("review_id") int review_id
	        ) throws java.io.IOException {

		// 파일 저장 경로 설정
	    String directory = "src/main/resources/upload/reviews/";
	    String originalFileName = file.getOriginalFilename();
	    String fileName = plan_id + "_" + originalFileName.substring(0, originalFileName.lastIndexOf(".")) + "_" +
	            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".jpg";
	    
	    File reviewDirectory = new File(directory + review_id);
	    
	    // 디렉토리가 존재하지 않으면 생성
	    if (!reviewDirectory.exists()) {
	        reviewDirectory.mkdirs();  // 디렉토리 생성
	    }
	    
	    File destination = new File(reviewDirectory, fileName);
	    
	    try {
	        // 파일 저장 로직
	        Files.copy(file.getInputStream(), Paths.get(destination.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
	        
	        ReviewImagesDto img = new ReviewImagesDto();
	        img.setReview_id(review_id);
	        img.setOriginal_name(originalFileName);
	        img.setStored_name(fileName);
	        img.setFile_path(directory);
	        img.setUploaded_at(LocalDateTime.now());
	        img.set_thumbnail(isThumbnail);
	        
	        Optional<Integer> result = travelReviewsService.saveReviewImage(img);
	        
	        if (result.isPresent() && result.get() != 0) {
	            return new ResponseEntity<>(HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


	// 리뷰 저장
	@PostMapping
	public ResponseEntity<?> saveReview(@RequestHeader("accessToken") String header,
			@RequestBody TravelReviewsDto reviewDto) {
		
		try {
			// accessToken에서 user_id 추출
			int user_id = userHelperService.getUserIdFromHeader(header);

			reviewDto.setUser_id(user_id);
			
			System.out.println("전달받은 reviewDto : "+reviewDto);

			// 서비스 호출
			int reviewId = travelReviewsService.saveReview(reviewDto);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(reviewId);

			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error saving review: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
		}
	}

	// 리뷰 전체 조회 
	@GetMapping
	public ResponseEntity<?> getReviewsWithThumbnail(@RequestParam(defaultValue = "date", required=false) String orderBy,
	                                     @RequestParam(required = false) Integer dog_size) {
	    try {
	        List<Map<String, Object>> reviews = travelReviewsService.getReviewsWithThumbnail(dog_size, orderBy);
	        return ResponseEntity.ok(reviews);
	    } catch (Exception e) {
	        log.error("Error fetching reviews: {}", e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
	    }
	}
	
	
	// 리뷰 상세 조회 - 이미지 전부 가져오기
	@GetMapping("/detail/{review_id}/images")
    public List<String> getReviewImages(@PathVariable int review_id) {
        // ReviewService의 getReviewImages 메서드를 호출하여 이미지 리스트 반환
        return travelReviewsService.getReviewImages(review_id);
    }
	
	// 리뷰 상세 조회
	@GetMapping("/detail/{id}")
    public TravelReviewsDto getReviewDetail(@PathVariable("id") int id) {
        return travelReviewsService.getReviewDetail(id);
    }
	
	// 좋아요 추가
    @PostMapping("/like/{id}")
    public ResponseEntity<String> incrementFavoriteCount(
            @RequestHeader("accessToken") String header,
            @PathVariable("id") int id) {
        
        // 헤더에서 user_id 추출
        int userId = userHelperService.getUserIdFromHeader(header);

        // 서비스 호출
        travelReviewsService.addFavorite(userId, id);

        return ResponseEntity.ok("Favorite added successfully!");
    }

    // 좋아요 취소
    @DeleteMapping("/like/{id}")
    public ResponseEntity<String> decrementFavoriteCount(
            @RequestHeader("accessToken") String header,
            @PathVariable("id") int id) {
        
        // 헤더에서 user_id 추출
        int userId = userHelperService.getUserIdFromHeader(header);

        // 서비스 호출
        travelReviewsService.removeFavorite(userId, id);

        return ResponseEntity.ok("Favorite removed successfully!");
    }
	
    // 사용자가 좋아요 눌렀는지 여부 확인할 수 있게
    @GetMapping("/like/user")
    public ResponseEntity<Boolean> checkUserLikedReview(
            @RequestParam("review_id") int reviewId,
            @RequestHeader("accessToken") String header) {
    	
    	// 헤더에서 user_id 추출
        int userId = userHelperService.getUserIdFromHeader(header);
        
        boolean isLiked = travelReviewsService.checkLiked(reviewId, userId);
        return ResponseEntity.ok(isLiked);
    }
    


}
