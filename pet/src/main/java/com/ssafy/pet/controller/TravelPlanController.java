package com.ssafy.pet.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ssafy.pet.dto.TravelPlanItemsDto;
import com.ssafy.pet.dto.TravelPlansDto;
import com.ssafy.pet.exception.ApplicationException;
import com.ssafy.pet.exception.errorcode.TravelPlanErrorCode;
import com.ssafy.pet.exception.errorcode.UserErrorCode;
import com.ssafy.pet.model.service.travelplan.TravelPlanService;
import com.ssafy.pet.util.JWTUtil;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/plan")
public class TravelPlanController {
	private final JWTUtil jwtUtil;
	private final TravelPlanService travelPlanService;

	@PostMapping
	public ResponseEntity<?> postPlans(@RequestHeader("accessToken") String header, @RequestBody ObjectNode request) throws RuntimeException, Exception {
		HttpStatus status = HttpStatus.ACCEPTED;

		ObjectMapper objectMapper = new ObjectMapper();

		Map<String, Object> resultMap = new HashMap<>();

		String user_id = jwtUtil.getUserId(header);

		Map<String, Object> params = new HashMap<>();

		TravelPlansDto plan = objectMapper.treeToValue(request.get("plan"), TravelPlansDto.class);

		List<TravelPlanItemsDto> items = new ArrayList<>();
		ArrayNode itemsNode = (ArrayNode) request.get("items");

		for (JsonNode node : itemsNode) {
			items.add(objectMapper.treeToValue(node, TravelPlanItemsDto.class));
		}

		params.put("u_id", user_id);
		params.put("plan", plan);
		params.put("items", items);

		int id = travelPlanService.insert(params).orElseThrow(() -> new RuntimeException());

		resultMap.put("plan_id", id);

		status = HttpStatus.CREATED;

		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@PutMapping("/{plan_id}")
	public ResponseEntity<?> updatePlans(@RequestHeader("accessToken") String header,
			@PathVariable("plan_id") String plan_id, @RequestBody ObjectNode request)
			throws JsonProcessingException, IllegalArgumentException {

		HttpStatus status = HttpStatus.ACCEPTED;
		ObjectMapper objectMapper = new ObjectMapper();

		String loginUserId = jwtUtil.getUserId(header);

		Map<String, Object> params = new HashMap<>();
		
		int id = Integer.parseInt(plan_id);
		TravelPlansDto existPlan = travelPlanService.findPlanByIdAndUserId(id, loginUserId);
		TravelPlansDto updatePlan = objectMapper.treeToValue(request.get("plan"), TravelPlansDto.class);

		List<TravelPlanItemsDto> items = new ArrayList<>();
		ArrayNode itemsNode = (ArrayNode) request.get("items");

		for (JsonNode node : itemsNode) {
			items.add(objectMapper.treeToValue(node, TravelPlanItemsDto.class));
		}

		params.put("user_id", existPlan.getUser_id());
		params.put("plan_id", id);
		params.put("plan", updatePlan);
		params.put("items", items);
				
		travelPlanService.update(params).orElseThrow(() -> new RuntimeException());
		status = HttpStatus.NO_CONTENT;

		return new ResponseEntity<>(status);
	}

	@PatchMapping("/{plan_id}/content")
	public ResponseEntity<?> patchPlanContent(@RequestHeader("accessToken") String header,
			@PathVariable("plan_id") String plan_id, @RequestBody ObjectNode request)
			throws JsonProcessingException, IllegalArgumentException {
		HttpStatus status = HttpStatus.ACCEPTED;

		ObjectMapper objectMapper = new ObjectMapper();
		System.out.println(plan_id);
		String loginUserId = jwtUtil.getUserId(header);

		System.out.println(loginUserId);

		Map<String, Object> params = new HashMap<>();
		
		int id = Integer.parseInt(plan_id);
		TravelPlansDto existPlan = travelPlanService.findPlanByIdAndUserId(id, loginUserId);
		TravelPlansDto plan = objectMapper.treeToValue(request.get("plan"), TravelPlansDto.class);

		params.put("plan_id", id);
		params.put("plan", plan);
		params.put("user_id", existPlan.getUser_id());

		travelPlanService.updatePlan(params).orElseThrow(() -> new RuntimeException());

		status = HttpStatus.NO_CONTENT;

		return new ResponseEntity<>(status);
	}

	@PatchMapping("/{plan_id}/route")
	public ResponseEntity<?> patchPlanRoute(@RequestHeader("accessToken") String header,
			@PathVariable("plan_id") String plan_id, @RequestBody ObjectNode request)
			throws JsonProcessingException, IllegalArgumentException {
		HttpStatus status = HttpStatus.ACCEPTED;
		ObjectMapper objectMapper = new ObjectMapper();

		String loginUserId = jwtUtil.getUserId(header);

		Map<String, Object> params = new HashMap<>();

		int id = Integer.parseInt(plan_id);
		travelPlanService.findPlanByIdAndUserId(id, loginUserId);

		List<TravelPlanItemsDto> items = new ArrayList<>();
		ArrayNode itemsNode = (ArrayNode) request.get("items");

		for (JsonNode node : itemsNode) {
			items.add(objectMapper.treeToValue(node, TravelPlanItemsDto.class));
		}

		params.put("items", items);
		params.put("plan_id", id);

		System.out.println(params);
		travelPlanService.updateItem(params).orElseThrow(() -> new RuntimeException());

		status = HttpStatus.NO_CONTENT;
		return new ResponseEntity<>(status);
	}
}