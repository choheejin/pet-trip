package com.ssafy.pet.model.service.attraction;

import java.util.List;
import java.util.Map;

import com.ssafy.pet.dto.AttractionDetailDto;
import com.ssafy.pet.dto.AttractionsDto;
import com.ssafy.pet.dto.GugunsDto;
import com.ssafy.pet.dto.HotplaceDto;
import com.ssafy.pet.dto.PetAttractionsDto;
import com.ssafy.pet.dto.TravelPlansDto;

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
	List<AttractionDetailDto> searchDetailByKeyword(String keyword, Integer page_start, Integer page_size);

	//content_id로 hotplace 찾기
	HotplaceDto searchHotplaceById(int content_id);

	// user_id로 user table의 primary-key인 id 찾기
	int searchUserByUserId(String user_id);

	// 핫플레이스로 장소 등록
	int addHotplace(int content_id, String user_id);
	
	// 핫플레이스 장소 삭제: key_content_id, value: user
	int deleteHotplace(int content_id, String user_id);
	
	//sido_code로 gugun_code 찾기
	List<GugunsDto> searchGugunCodeBySidoCode(int sido_code);

	//favorite cnt를 기준으로 내림차순으로 정렬된 여행계획 가져오기
	List<TravelPlansDto> getPlanRanking(Integer page_start, Integer page_size);
	
	//가장 많이 좋아요 언급된 핫플레이스를 내림차순으로 정렬해서 가져오기
	List<AttractionsDto> getHotplaceRanking(Integer page_start, Integer page_size);

	//travel plan items를 plan_id로 조회해서 attractions의 이미지 가져오기
	String getFirstImageByPlanId(int plan_id);
	
	//travel_plan dto의 first_image를 지정해주는 함수
	void setPlanImage(List<TravelPlansDto> plans);
	
	//user_id를 통해서 hotplace의 content_id 찾기
	List<Integer> listHotplaceContentIdsByUserId(String user_id);
	
	// 핫플레이스 목록 보기
	List<AttractionsDto> viewHotplaces();
	
	// 사용자 id와 content_id를 통해 좋아요를 눌렀는지 확인한다
	boolean getUserLikedContent(String user_id, int content_id);
}
