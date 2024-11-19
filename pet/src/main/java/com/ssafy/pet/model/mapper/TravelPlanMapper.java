package com.ssafy.pet.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
	
	TravelPlansDto findPlanById(int id);
	
	TravelPlansDto findPlanByUserIdAndId(Map<String, Object> param);
	
	List<TravelPlansDto> selectWithLimit(Integer page);
	
	//오래된 순으로 게시글 조회
	List<TravelPlansDto> getOldestPlans(@Param("page_start") int page_start, @Param("page_size") int page_size);
	
	//최신순으로 게시글 조회
	List<TravelPlansDto> getNewestPlans(@Param("page_start") int page_start, @Param("page_size") int page_size);
	
	//가장 많이 조회 된 순으로 게시글 조회
	List<TravelPlansDto> getPlansByMostViews(@Param("page_start") int page_start, @Param("page_size") int page_size);
	
	//게시글의 댓글 조회
	List<String> getComments(int plan_id);
	
	List<Map<String, Object>> findItemById(int id);
}
