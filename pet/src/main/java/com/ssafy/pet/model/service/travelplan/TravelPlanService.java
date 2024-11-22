package com.ssafy.pet.model.service.travelplan;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.ssafy.pet.dto.PlansFavoritesDto;
import com.ssafy.pet.dto.TravelPlansDto;

public interface TravelPlanService {
	Optional<Integer> insert(Map<String, Object> parmas) throws Exception;
	
	Optional<Integer> insertPlan(Map<String, Object> parmas);
	
	Optional<Integer> insertItem(Map<String, Object> params);
	
	Optional<Integer> update(Map<String, Object> params);
	
	Optional<Integer> updatePlan(Map<String, Object> params);
	
	Optional<Integer> updateItem(Map<String, Object> parmas);
	
	TravelPlansDto findPlanByIdAndUserId(int id, String userId);
	
	Optional<Map<String, Object>> findPlanWithItemsById(int id); 
	
	//오래된 순으로 게시글 조회
	List<TravelPlansDto> getOldestPlans(int page_start, int page_size);
	
	//최신순으로 게시글 조회
	List<TravelPlansDto> getNewestPlans(int page_start, int page_size);
	
	//가장 많이 조회 된 순으로 게시글 조회
	List<TravelPlansDto> getPlansByMostViews(int page_start, int page_size);
	
	//유저가 게시한 게시글 조회
	List<TravelPlansDto> getUserPlans(int user_id);
	
	//유저가 좋아요한 게시글 조회
	List<TravelPlansDto> getUserFavoritePlans(int user_id);
	
	//게시글의 댓글 조회
	List<String> getComments(int plan_id);
	
	//정렬 타입별 게시글 조회
	List<TravelPlansDto> getPlansBySort(String sort, int page_start, int page_size);
	
	//페이지네이션용 게시글 조회
	List<TravelPlansDto> getAllPlansBySort(String sort);
	
	//유저가 좋아요 누른 게시글 plans_favorites에 추가하기
	int addFavoritePlan(int user_id, int favorite_plan_id);
	
	//유저가 좋아요 누른 게시글 삭제하기
	int deleteFavoritePlan(int user_id, int favorite_plan_id);
	
	//유저가 좋아요 누른 게시글 상태 계산
	boolean[] calculateFavoriteStatus(List<TravelPlansDto> plans, int user_id);
}
