package com.ssafy.pet.model.service.travelplan;

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
}