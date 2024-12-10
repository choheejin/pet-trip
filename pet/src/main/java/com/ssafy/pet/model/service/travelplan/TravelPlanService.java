package com.ssafy.pet.model.service.travelplan;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.ssafy.pet.dto.PlansFavoritesDto;
import com.ssafy.pet.dto.TravelPlanCommentsDto;
import com.ssafy.pet.dto.TravelPlanCommentsRequestDto;
import com.ssafy.pet.dto.TravelPlansDto;

public interface TravelPlanService {
	Optional<Integer> insert(Map<String, Object> parmas) throws Exception;
	
	Optional<Integer> insertPlan(Map<String, Object> parmas);
	
	Optional<Integer> insertItem(Map<String, Object> params);
	
	Optional<Integer> update(Map<String, Object> params);
	
	Optional<Integer> updatePlan(Map<String, Object> params);
	
	Optional<Integer> updateItem(Map<String, Object> parmas);
	
	Optional<Integer> delete(int plan_id);
	
	TravelPlansDto findPlanByIdAndUserId(int id, String userId);
	
	Optional<Map<String, Object>> findPlanWithItemsById(String user_id, int id); 
	
	//오래된 순으로 게시글 조회
	List<TravelPlansDto> getOldestPlans(int page_start, int page_size);
	
	//최신순으로 게시글 조회
	List<TravelPlansDto> getNewestPlans(int page_start, int page_size);
	
	//가장 많이 조회 된 순으로 게시글 조회
	List<TravelPlansDto> getPlansByMostViews(int page_start, int page_size);
	
	//유저가 게시한 게시글 조회
	List<TravelPlansDto> getUserPlans(String user_id);
	
	//유저가 좋아요한 게시글 조회
	List<TravelPlansDto> getUserFavoritePlans(String user_id);
	
	List<TravelPlanCommentsRequestDto> convertToCommentsRequestDto(List<TravelPlanCommentsDto> plans);
	
	/////////////////////////////////////
	//게시글의 댓글 조회
	List<TravelPlanCommentsDto> listParentComments(int plan_id);
	
	//대댓글 가져오기
	List<TravelPlanCommentsRequestDto> listChildComments(int parent_comment_id);
	
	//댓글 작성
	TravelPlanCommentsRequestDto postComment(TravelPlanCommentsDto comment);
	
	//댓글 삭제
	int deleteComment(int comment_pk, String userId);
	
	//유저가 작성한 댓글인지 확인하기위해 comment_pk의 user_id 가져오기
	int findUserIdByCommentId(int comment_pk);
	////////////////////////////////////
	
	//게시글 조회수 증가
	Optional<Integer> increasePlanViewCnt(int plan_id);
	
	//정렬 타입별 게시글 조회
	List<TravelPlansDto> getPlansBySort(String sort, int page_start, int page_size);
	
	//페이지네이션용 게시글 조회
	List<TravelPlansDto> getAllPlansBySort(String sort);
	
	//유저가 좋아요 누른 게시글 plans_favorites에 추가하기
	int addFavoritePlan(String user_id, int favorite_plan_id);
	
	//유저가 좋아요 누른 게시글 삭제하기
	int deleteFavoritePlan(String user_id, int favorite_plan_id);
	
	//유저가 좋아요 누른 게시글 상태 계산
	boolean[] calculateFavoriteStatus(List<TravelPlansDto> plans, String user_id);
	
	//유저가 특정 게시글을 좋아요를 눌렀었는지 확인
	boolean getUserLikedPlan(String user_id, int plan_id);
}
