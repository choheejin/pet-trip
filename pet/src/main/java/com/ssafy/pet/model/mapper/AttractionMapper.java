package com.ssafy.pet.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.pet.dto.AttractionDetailDto;
import com.ssafy.pet.dto.AttractionsDto;
import com.ssafy.pet.dto.PetAttractionsDto;

@Mapper
public interface AttractionMapper {
	// 관광지 검색 모드
	List<AttractionsDto> searchAttractions(Map<String, Object> params);

	// 관광지 상세 검색
	AttractionDetailDto detailAttraction(int content_id);

	// 관광지 content_id 로 찾기
	AttractionsDto searchByContentID(int content_id);

	// 반려견 동반 관광지 content_id로 찾기
	PetAttractionsDto searchPetByContentID(int content_id);

	// 핫플레이스로 장소 등록
	int addHotplace(int content_id);

	// 핫플레이스 목록 보기
	List<AttractionsDto> viewHotplaces();
}
