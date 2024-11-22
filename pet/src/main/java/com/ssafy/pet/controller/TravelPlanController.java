package com.ssafy.pet.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ssafy.pet.config.PaginationConstants;
import com.ssafy.pet.dto.PaginatedResponseDto;
import com.ssafy.pet.dto.PlansFavoritesDto;

import com.ssafy.pet.dto.TravelPlanItemsDto;
import com.ssafy.pet.dto.TravelPlansDto;
import com.ssafy.pet.dto.UserPlansResponseDto;
import com.ssafy.pet.model.service.attraction.AttractionService;
import com.ssafy.pet.model.service.travelplan.TravelPlanService;
import com.ssafy.pet.model.service.user.UserHelperService;
import com.ssafy.pet.util.JWTUtil;
import com.ssafy.pet.util.UtilClass;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plan")
public class TravelPlanController {
	private final JWTUtil jwtUtil;
	private final TravelPlanService travelPlanService;
	private final AttractionService attracionService;
	private final UserHelperService userHelperService;

	@GetMapping("/naver-map")
	public ResponseEntity<?> getNaverMap(@RequestParam(value = "start") String start,
			@RequestParam(value = "goal") String goal, @RequestParam(value = "waypoints") String waypoints) {
		RestTemplate restTemplate = new RestTemplate();

		String url = "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving";
		String uri = UriComponentsBuilder.fromHttpUrl(url).queryParam("start", start).queryParam("goal", goal)
				.queryParam("waypoints", waypoints).toUriString();

		System.out.println("naverapi 요청 uri:: " + uri);

		HttpHeaders headers = new HttpHeaders();
		headers.set("X-NCP-APIGW-API-KEY-ID", "pg2yaio94a");
		headers.set("X-NCP-APIGW-API-KEY", "mwsnJqT5q0e3OWJY9EhbzCJ4r4c835VUndpHFqOS");

		HttpEntity request = new HttpEntity(headers);
		System.out.println("naverapi 요청 headers:: " + headers);

		ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET, request, Map.class);
		
		return response;
	}

	@GetMapping
	public ResponseEntity<?> getPlans(@RequestParam(required = false) Integer page) {
		HttpStatus status = HttpStatus.ACCEPTED;

		Map<String, Object> resultMap = new HashMap<>();
		List<TravelPlansDto> list = travelPlanService.selectWithLimit(page).orElseThrow(() -> new RuntimeException());

		resultMap.put("list", list);
		status = HttpStatus.OK;

		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@GetMapping("/{plan_id}")
	public ResponseEntity<?> getPlanById(@PathVariable("plan_id") Integer plan_id) {
		HttpStatus status = HttpStatus.ACCEPTED;

		Map<String, Object> resultMap = travelPlanService.findPlanWithItemsById(plan_id)
				.orElseThrow(() -> new RuntimeException());

		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@GetMapping("/plans")
	public ResponseEntity<UserPlansResponseDto> getPlans(
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "sort", required = false, defaultValue = "oldest") String sort,
			@RequestHeader("accessToken") String header) {


		int page_start = UtilClass.caculateOffest(page);
		List<TravelPlansDto> sortedPlan = travelPlanService.getPlansBySort(sort, page_start,
				PaginationConstants.PAGE_SIZE);
		List<TravelPlansDto> allRes = travelPlanService.getAllPlansBySort(sort);

		attracionService.setPlanImage(sortedPlan);
		int total_pages = UtilClass.calculateTotalPages(allRes.size());

		UserPlansResponseDto res = new UserPlansResponseDto();

		if(header != null)
		{
			int id = userHelperService.getUserIdFromHeader(header);
			boolean[] userFavoriteStatus = travelPlanService.calculateFavoriteStatus(sortedPlan, id);
			res.setFavoritePlans(userFavoriteStatus);			
		}
		
		res.setPlans(sortedPlan);
		res.setTotal_pages(total_pages);

		return ResponseEntity.ok(res);
	}

	@GetMapping("/comments")
	@ResponseBody
	public ResponseEntity<List<String>> getComments(@RequestParam(value = "plan_id") int plan_id) {
		List<String> comments = new ArrayList<>();

		comments = travelPlanService.getComments(plan_id);

		return ResponseEntity.ok(comments);
	}

	@PostMapping
	public ResponseEntity<?> postPlans(@RequestHeader("accessToken") String header, @RequestBody ObjectNode request)
			throws RuntimeException, Exception {
		HttpStatus status = HttpStatus.ACCEPTED;

		ObjectMapper objectMapper = new ObjectMapper();

		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> params = new HashMap<>();
		List<TravelPlanItemsDto> items = new ArrayList<>();

		String user_id = jwtUtil.getUserId(header);
		TravelPlansDto plan = objectMapper.treeToValue(request.get("plan"), TravelPlansDto.class);
		ArrayNode itemsNode = (ArrayNode) request.get("items");

		int id;

		for (JsonNode node : itemsNode) {
			items.add(objectMapper.treeToValue(node, TravelPlanItemsDto.class));
		}

		params.put("u_id", user_id);
		params.put("plan", plan);
		params.put("items", items);

		id = travelPlanService.insert(params).orElseThrow(() -> new RuntimeException());

		resultMap.put("plan_id", id);

		status = HttpStatus.CREATED;

		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@PostMapping("/add-user-favorite-plan")
	public ResponseEntity<?> addUserFavoritePlan(@RequestBody PlansFavoritesDto pf)
	{
		HttpStatus status = HttpStatus.ACCEPTED;
		
		int res = travelPlanService.addFavoritePlan(pf);
		
		if(res < 0) 
		{
			throw new RuntimeException();
		}
		
		status = HttpStatus.CREATED;
		
		return ResponseEntity.ok(status);
	}
	
	@DeleteMapping("/delete-user-favorite-plan")
	public ResponseEntity<?> deleteUserFavoritePlan(@RequestHeader("accessToken") String header, @RequestBody PlansFavoritesDto pf)
	{
		HttpStatus status = HttpStatus.ACCEPTED;
		int id = userHelperService.getUserIdFromHeader(header);
		
		if(id == pf.getUser_id())
		{
			int res = travelPlanService.deleteFavoritePlan(pf);
			
			if(res < 0) 
			{
				throw new RuntimeException();
			}
			
			status = HttpStatus.NO_CONTENT;
			
			return ResponseEntity.ok(status);	
		}
		else
		{
			return ResponseEntity
					.status(HttpStatus.FORBIDDEN)
					.body("좋아요 취소 할 수 있는 유저가 아닙니다.");			
		}
	}
	@GetMapping("/user-plan")
	@ResponseBody
	public ResponseEntity<List<TravelPlansDto>> userPlan(@RequestHeader("accessToken") String header) {

		int id = userHelperService.getUserIdFromHeader(header);

		List<TravelPlansDto> result = travelPlanService.getUserPlans(id);

		return ResponseEntity.ok(result);
	}

	@GetMapping("/user-favorite-plans")
	@ResponseBody
	public ResponseEntity<List<TravelPlansDto>> userFavoritePlans(@RequestHeader("accessToken") String header,
			@RequestBody List<TravelPlansDto> plans) {

		int id = userHelperService.getUserIdFromHeader(header);

		List<TravelPlansDto> result = travelPlanService.getUserFavoritePlans(id);

		return ResponseEntity.ok(result);
	}

	@PutMapping("/{plan_id}")
	public ResponseEntity<?> updatePlans(@PathVariable("plan_id") String plan_id,
			@RequestHeader("accessToken") String header, @RequestBody ObjectNode request)
			throws JsonProcessingException, IllegalArgumentException {

		HttpStatus status = HttpStatus.ACCEPTED;

		ObjectMapper objectMapper = new ObjectMapper();

		Map<String, Object> params = new HashMap<>();
		List<TravelPlanItemsDto> items = new ArrayList<>();

		int id = Integer.parseInt(plan_id);

		String loginUserId = jwtUtil.getUserId(header);

		TravelPlansDto existPlan = travelPlanService.findPlanByIdAndUserId(id, loginUserId);
		TravelPlansDto updatePlan = objectMapper.treeToValue(request.get("plan"), TravelPlansDto.class);
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

		int id = Integer.parseInt(plan_id);

		String loginUserId = jwtUtil.getUserId(header);

		Map<String, Object> params = new HashMap<>();

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

		int id = Integer.parseInt(plan_id);
		String loginUserId = jwtUtil.getUserId(header);

		Map<String, Object> params = new HashMap<>();
		List<TravelPlanItemsDto> items = new ArrayList<>();

		travelPlanService.findPlanByIdAndUserId(id, loginUserId);

		ArrayNode itemsNode = (ArrayNode) request.get("items");

		for (JsonNode node : itemsNode) {
			items.add(objectMapper.treeToValue(node, TravelPlanItemsDto.class));
		}

		params.put("items", items);
		params.put("plan_id", id);

		travelPlanService.updateItem(params).orElseThrow(() -> new RuntimeException());

		status = HttpStatus.NO_CONTENT;

		return new ResponseEntity<>(status);
	}
}
