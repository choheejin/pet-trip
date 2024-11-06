package com.ssafy.pet.model.service.attraction;

import java.util.List;
import java.util.Map;

import com.ssafy.pet.dto.AttractionDetailDto;
import com.ssafy.pet.dto.AttractionsDto;

public interface AttractionService {
	// 관광지 검색 모드
	List<AttractionsDto> searchAttractions(Map<String, Object> params);

	// 관광지 상세 검색
	AttractionDetailDto detailAttraction(int content_id);

	// 핫플레이스로 장소 등록
	int addHotplace(int content_id);

	// 핫플레이스 목록 보기
	List<AttractionsDto> viewHotplaces();
}
