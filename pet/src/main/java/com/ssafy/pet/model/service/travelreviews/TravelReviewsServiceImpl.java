package com.ssafy.pet.model.service.travelreviews;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.pet.dto.ReviewImagesDto;
import com.ssafy.pet.dto.TravelReviewsDto;
import com.ssafy.pet.model.mapper.TravelReviewsMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelReviewsServiceImpl implements TravelReviewsService {
	private final TravelReviewsMapper travelReviewsMapper;
	
	public Optional<Integer> saveReview(TravelReviewsDto reviewDto, List<MultipartFile> files) {
		// 프론트에서 전달하는 thumbnail은 ReviewImagesDto 형태고,
		// TravelReviewsDto 의 thumbnail은 String 타입으로 새로운 storedname을 저장해야함!
		
		// 1. travel_reviews 테이블에 리뷰 저장
//		ReviewImagesDto thumbnail = reviewDto.getThumbnail_image();
//		reviewDto.set
		int reviewId = travelReviewsMapper.saveReview(reviewDto);
        
        // 리뷰 저장 성공 후에
		if (reviewId != 0) {
			// 2. 리뷰 이미지 저장
			if (files != null && !files.isEmpty()) {
				int cnt = 1;
				for (MultipartFile file : files) {
					String imageFileName = saveReviewImage(file, reviewId, cnt);
					
					// 이미지 정보 셋팅
					ReviewImagesDto imageDto = new ReviewImagesDto();
					imageDto.setReview_id(reviewId);
					imageDto.setOriginal_name(file.getOriginalFilename());
					imageDto.setFile_path("src/main/resources/upload/review/"+reviewId+"/");
					imageDto.setStored_name(imageFileName);
					imageDto.setUploaded_at(LocalDateTime.now());
					
					// 3. 테이블에 이미지 저장
					travelReviewsMapper.saveReviewImage(imageDto);
				}
			}
		}
        return reviewId > 0 ? Optional.of(reviewId) : Optional.empty();
	}
	
	// 이미지를 저장하고 파일명을 반환하는 메서드
    private String saveReviewImage(MultipartFile file, Integer reviewId, int cnt) {
        String directory = "src/main/resources/upload/review/"+reviewId+"/";
        String extension = ".jpg";  // 이미지 확장자는 필요에 따라 동적으로 처리 가능
        String fileName = reviewId + "_" + cnt + extension;

        File destination = new File(directory + fileName);

        try {
            Files.copy(file.getInputStream(), Paths.get(destination.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

	@Override
	public List<TravelReviewsDto> getReviews(String orderBy, Integer dogSize) {
		
		return travelReviewsMapper.getReviews(orderBy, dogSize);
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
