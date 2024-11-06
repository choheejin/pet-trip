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

	@PostMapping("/{user_id}")
	public ResponseEntity<?> postPlans(@PathVariable("user_id") String user_id, @RequestBody ObjectNode request) {
		HttpStatus status = HttpStatus.ACCEPTED;

		ObjectMapper objectMapper = new ObjectMapper();

		Map<String, Object> resultMap = new HashMap<>();

		try {
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

	@PutMapping("/{plan_id}")
	public ResponseEntity<?> updatePlans(@RequestHeader("accessToken") String header,
			@PathVariable("plan_id") String plan_id, @RequestBody ObjectNode request) throws JsonProcessingException, IllegalArgumentException {

		HttpStatus status = HttpStatus.ACCEPTED;
		ObjectMapper objectMapper = new ObjectMapper();

		String loginUserId = jwtUtil.getUserId(header);

		Map<String, Object> params = new HashMap<>();

		String writerId = objectMapper.treeToValue(request.get("writer_id"), String.class);
		if (!loginUserId.equals(writerId))
			throw new ApplicationException(UserErrorCode.UNAUTHORIZED);

		TravelPlansDto plan = objectMapper.treeToValue(request.get("plan"), TravelPlansDto.class);

		List<TravelPlanItemsDto> items = new ArrayList<>();
		ArrayNode itemsNode = (ArrayNode) request.get("items");

		for (JsonNode node : itemsNode) {
			items.add(objectMapper.treeToValue(node, TravelPlanItemsDto.class));
		}

		params.put("user_id", writerId);
		params.put("plan_id", plan_id);
		params.put("plan", plan);
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

		String writerId = objectMapper.treeToValue(request.get("writer_id"), String.class);
		TravelPlansDto plan = objectMapper.treeToValue(request.get("plan"), TravelPlansDto.class);

		// 수정하려고 하는 이와 작성자가 동일한지 확인한다.
		if (!loginUserId.equals(writerId))
			throw new ApplicationException(UserErrorCode.UNAUTHORIZED);

		params.put("user_id", writerId);
		params.put("plan", plan);
		params.put("plan_id", plan_id);

		travelPlanService.updatePlan(params).orElseThrow(() -> new RuntimeException());

		status = HttpStatus.NO_CONTENT;

		return new ResponseEntity<>(status);
	}

	@PatchMapping("/{plan_id}/route")
	public ResponseEntity<?> patchPlanRoute(@RequestHeader("accessToken") String header,
			@PathVariable("plan_id") String plan_id, @RequestBody ObjectNode request) throws JsonProcessingException, IllegalArgumentException {
		HttpStatus status = HttpStatus.ACCEPTED;
		ObjectMapper objectMapper = new ObjectMapper();

		String loginUserId = jwtUtil.getUserId(header);

		Map<String, Object> params = new HashMap<>();

		String writerId = objectMapper.treeToValue(request.get("writer_id"), String.class);

		// 수정하려고 하는 이와 작성자가 동일한지 확인한다.
		if (!loginUserId.equals(writerId))
			throw new ApplicationException(UserErrorCode.UNAUTHORIZED);

		List<TravelPlanItemsDto> items = new ArrayList<>();
		ArrayNode itemsNode = (ArrayNode) request.get("items");

		for (JsonNode node : itemsNode) {
			items.add(objectMapper.treeToValue(node, TravelPlanItemsDto.class));
		}

		params.put("items", items);
		params.put("plan_id", Integer.parseInt(plan_id));

		System.out.println(params);
		travelPlanService.updateItem(params).orElseThrow(() -> new RuntimeException());

		return new ResponseEntity<>(status);
	}
}
