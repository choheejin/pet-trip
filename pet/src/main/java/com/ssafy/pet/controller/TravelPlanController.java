package com.ssafy.pet.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ssafy.pet.dto.TravelPlanItemsDto;
import com.ssafy.pet.dto.TravelPlansDto;
import com.ssafy.pet.model.service.travelplan.TravelPlanService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/plan")
public class TravelPlanController {
	private final TravelPlanService travelPlanService;
	
	@PostMapping("/{user_id}")
	public ResponseEntity<?> postPlans(
			@PathVariable("user_id") String user_id,
			@RequestBody ObjectNode request) {
		HttpStatus status = HttpStatus.ACCEPTED;
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		Map<String, Object> resultMap = new HashMap<>();

		try {
			Map<String, Object> params = new HashMap<>();
			
			TravelPlansDto plan = objectMapper.treeToValue(request.get("plan"), TravelPlansDto.class);
			
			List<TravelPlanItemsDto> items = new ArrayList<>();
			ArrayNode itemsNode = (ArrayNode) request.get("items");			
			
			for(JsonNode node: itemsNode) {
				items.add(objectMapper.treeToValue(node, TravelPlanItemsDto.class));
			}
			
			params.put("u_id", user_id);
			params.put("plan", plan);
			params.put("items", items);
			
			
			int id = travelPlanService.insert(params).orElseThrow(() -> new Exception());
			
			resultMap.put("plan_id", id);
			
			status = HttpStatus.CREATED;
		} catch (JsonProcessingException | IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
}
