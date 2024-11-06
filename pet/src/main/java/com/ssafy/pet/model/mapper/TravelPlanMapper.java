package com.ssafy.pet.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.pet.dto.TravelPlanItemsDto;

@Mapper
public interface TravelPlanMapper {
	// 게시글 등록
	int insertPlan(Map<String, Object> param);
	// 여행 경로 등록
	int insertPlanItem(Map<String, Object> param);
	
	// 여행 게시판 내용 수정
	int updatePlan(Map<String, Object> parm);
	
	int updatePlanItem(Map<String, Object> param);
}
