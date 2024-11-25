package com.ssafy.pet.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.pet.dto.PlansFavoritesDto;
import com.ssafy.pet.dto.TravelPlanCommentsDto;
import com.ssafy.pet.dto.TravelPlanItemsDto;
import com.ssafy.pet.dto.TravelPlansDto;

@Mapper
public interface TravelPlanMapper {
	// 게시글 등록
	int insertPlan(Map<String, Object> param);
	// 여행 경로 등록
	int insertPlanItem(Map<String, Object> param);
	
	// 여행 게시판 내용 수정
	int updatePlan(Map<String, Object> parm);
	
	int updatePlanItem(Map<String, Object> param);
	
	int deletePlan(int plan_id);
	
	TravelPlansDto findPlanById(int id);
	
	TravelPlansDto findPlanByUserIdAndId(Map<String, Object> param);
	
	List<TravelPlansDto> selectWithLimit(Integer page);
	
	//오래된 순으로 게시글 조회
	List<TravelPlansDto> getOldestPlans(@Param("page_start") int page_start, @Param("page_size") int page_size);
	
	//최신순으로 게시글 조회
	List<TravelPlansDto> getNewestPlans(@Param("page_start") int page_start, @Param("page_size") int page_size);
	
	//가장 많이 조회 된 순으로 게시글 조회
	List<TravelPlansDto> getPlansByMostViews(@Param("page_start") int page_start, @Param("page_size") int page_size);
	
	///////////////////댓글
	//게시글의 본 댓글만(대댓글 제외) 조회 - 시간순으로
	List<TravelPlanCommentsDto> listParentComments(int plan_id);
	
	//대댓글들 가져오기
	List<TravelPlanCommentsDto> listChildComments(int parent_comment_id);
	
	//댓글 작성
	int postComment(TravelPlanCommentsDto comment);
	
	//댓글 삭제
	int deleteComment(int comment_pk);
	
	//유저가 작성한 댓글인지 확인하기위해 comment_pk의 user_id 가져오기
	int findUserIdByCommentId(int comment_pk);
	////////////////////////////
	
	//유저가 게시한 게시글 조회
	List<TravelPlansDto> getUserPlans(int user_id);
	
	///////////////////////////////////////////////////////
	//유저가 좋아요한 게시글 조회
	List<TravelPlansDto> getUserFavoritePlans(int user_id);

	//유저가 좋아요한 게시글 아이디 조회
	List<Integer> getUserFavoritePlanIds(int user_id);
	/////////////////////////////////////////////////////
	
	//게시글 조회수 증가
	int increasePlanViewCnt(int plan_id);
	
	//유저가 좋아요 누른 게시글 plans_favorites에 추가하기
	int addFavoritePlan(@Param("user_id") int user_id, @Param("plan_id") int favorite_plan_id);
	
	//유저가 좋아요 누른 게시글 삭제하기
	int deleteFavoritePlan(@Param("user_id") int user_id, @Param("plan_id") int favorite_plan_id);
	
	List<Map<String, Object>> findItemById(int id);
	
	//유저가 특정 게시글을 좋아요를 눌렀었는지 확인
	int getUserLikedPlan(@Param("user_id") int user_id, @Param("plan_id") int plan_id);
}
