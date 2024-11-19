package com.ssafy.pet.model.service.travelplan;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.ssafy.pet.dto.TravelPlansDto;

public interface TravelPlanService {
	Optional<Integer> insert(Map<String, Object> parmas) throws Exception;
	
	Optional<Integer> insertPlan(Map<String, Object> parmas);
	
	Optional<Integer> insertItem(Map<String, Object> params);
	
	Optional<Integer> update(Map<String, Object> params);
	
	Optional<Integer> updatePlan(Map<String, Object> params);
	
	Optional<Integer> updateItem(Map<String, Object> parmas);
	
	TravelPlansDto findPlanByIdAndUserId(int id, String userId);
	
	Optional<List<TravelPlansDto>> selectWithLimit(Integer page);

	Optional<Map<String, Object>> findPlanWithItemsById(int id); 
	
	//오래된 순으로 게시글 조회
	List<TravelPlansDto> getOldestPlans(int page_start, int page_size);
	
	//최신순으로 게시글 조회
	List<TravelPlansDto> getNewestPlans(int page_start, int page_size);
	
	//가장 많이 조회 된 순으로 게시글 조회
	List<TravelPlansDto> getPlansByMostViews(int page_start, int page_size);
	
	//게시글의 댓글 조회
	List<String> getComments(int plan_id);
}
