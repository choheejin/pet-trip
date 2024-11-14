package com.ssafy.pet.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

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
	
	List<Map<String, Object>> findItemById(int id);
}
