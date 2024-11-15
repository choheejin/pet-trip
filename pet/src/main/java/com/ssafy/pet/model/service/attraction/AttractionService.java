package com.ssafy.pet.model.service.attraction;

import java.util.List;
import java.util.Map;

import com.ssafy.pet.dto.AttractionDetailDto;
import com.ssafy.pet.dto.AttractionsDto;
import com.ssafy.pet.dto.PetAttractionsDto;

public interface AttractionService {
	// 관광지 검색 모드
	List<AttractionsDto> searchAttractions(Map<String, Object> params);

	// 관광지 상세 검색
	AttractionDetailDto detailAttraction(int content_id);

	// 관광지 content_id 로 찾기
	AttractionsDto searchByContentID(int content_id);
	
	// 반려견 동반 관광지 content_id로 찾기
	PetAttractionsDto searchPetByContentID(int content_id);
	
	//반려견 타입으로 검색
	List<AttractionDetailDto> searchDetailByKeyword(String keyword);

	// 핫플레이스로 장소 등록
	int addHotplace(int content_id);

	// 핫플레이스 목록 보기
	List<AttractionsDto> viewHotplaces();
}
