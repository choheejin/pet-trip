package com.ssafy.pet.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.ssafy.pet.dto.TravelPlanCommentsDto;
import com.ssafy.pet.dto.TravelPlanCommentsRequestDto;
import com.ssafy.pet.dto.TravelPlanItemsDto;
import com.ssafy.pet.dto.TravelPlansDto;
import com.ssafy.pet.dto.UserPlansResponseDto;
import com.ssafy.pet.model.service.attraction.AttractionService;
import com.ssafy.pet.model.service.travelplan.TravelPlanService;
import com.ssafy.pet.model.service.user.UserHelperService;
import com.ssafy.pet.model.service.user.UserService;
import com.ssafy.pet.util.JWTUtil;
import com.ssafy.pet.util.UtilClass;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plan")
@Slf4j
public class TravelPlanController {
	private final JWTUtil jwtUtil;
	private final TravelPlanService travelPlanService;
	private final AttractionService attracionService;
	private final UserHelperService userHelperService;
	private final UserService userService;

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

	@GetMapping("/{plan_id}")
	public ResponseEntity<?> getPlanById(
			@RequestHeader(value = "accessToken", required = false) Optional<String> header,
			@PathVariable("plan_id") Integer plan_id) {
		HttpStatus status = HttpStatus.ACCEPTED;

		Map<String, Object> resultMap = travelPlanService.findPlanWithItemsById(plan_id)
				.orElseThrow(() -> new RuntimeException());
		
		if(!header.isEmpty()) {
			String user_id = jwtUtil.getUserId(header.get());
			boolean isLiked = travelPlanService.getUserLikedPlan(user_id, plan_id);
			resultMap.put("isLiked", isLiked);
		}

		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@GetMapping("/plans")
	public ResponseEntity<UserPlansResponseDto> getPlans(
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "sort", required = false, defaultValue = "oldest") String sort,
			@RequestHeader(value = "accessToken", required = false) String header) {

		int page_start = UtilClass.caculateOffest(page);
		List<TravelPlansDto> sortedPlan = travelPlanService.getPlansBySort(sort, page_start,
				PaginationConstants.PAGE_SIZE);
		List<TravelPlansDto> allRes = travelPlanService.getAllPlansBySort(sort);

		attracionService.setPlanImage(sortedPlan);
		int total_pages = UtilClass.calculateTotalPages(allRes.size());

		UserPlansResponseDto res = new UserPlansResponseDto();

		if (header != null && !header.isEmpty()) {
			int id = userHelperService.getUserIdFromHeader(header);
			boolean[] userFavoriteStatus = travelPlanService.calculateFavoriteStatus(sortedPlan, id);
			res.setFavoritePlans(userFavoriteStatus);
		} else {
			boolean[] defaultFavoriteStatus = new boolean[PaginationConstants.PAGE_SIZE];
			res.setFavoritePlans(defaultFavoriteStatus);
		}

		res.setPlans(sortedPlan);
		res.setTotal_pages(total_pages);

		
		return ResponseEntity.ok(res);
	}

	@GetMapping("/parent-comments")
	@ResponseBody
	public ResponseEntity<List<TravelPlanCommentsRequestDto>> getComments(@RequestParam(value = "plan_id") int plan_id) {
		List<TravelPlanCommentsDto> comments = new ArrayList<>();
		List<TravelPlanCommentsRequestDto> convertedComments = new ArrayList<>();
		
		comments = travelPlanService.listParentComments(plan_id);
		convertedComments = travelPlanService.convertToCommentsRequestDto(comments);

		return ResponseEntity.ok(convertedComments);
	}
	
	@GetMapping("/child-comments")
	@ResponseBody
	public ResponseEntity<List<TravelPlanCommentsRequestDto>> listChildComments(@RequestParam(value="parent_comment_id") int parent_comment_id)
	{
		List<TravelPlanCommentsDto> childComments = new ArrayList<>();
		List<TravelPlanCommentsRequestDto> convertedComments = new ArrayList<>();
		
		childComments = travelPlanService.listChildComments(parent_comment_id);
		convertedComments = travelPlanService.convertToCommentsRequestDto(childComments);
		
		return ResponseEntity.ok(convertedComments);
	}
	
	@PatchMapping("/increase-plan-view-cnt")
	public ResponseEntity<?> increasePlanViewCnt(@RequestParam(value="plan_id") int plan_id){
		HttpStatus status = HttpStatus.ACCEPTED;
		
		travelPlanService.increasePlanViewCnt(plan_id).orElseThrow(() -> new RuntimeException());

		status = HttpStatus.NO_CONTENT;
		
		return ResponseEntity.ok(status);
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
	public ResponseEntity<?> addUserFavoritePlan(@RequestHeader("accessToken") String header,
			@RequestParam(value = "plan_id") int plan_id) {
		int id = userHelperService.getUserIdFromHeader(header);
		HttpStatus status = HttpStatus.ACCEPTED;
		log.trace("addUserFavoritePlan : {}", plan_id);

		int res = travelPlanService.addFavoritePlan(id, plan_id);

		if (res <= 0) {
			throw new RuntimeException();
		}

		status = HttpStatus.CREATED;

		return ResponseEntity.ok(status);
	}

	@DeleteMapping("/delete-user-favorite-plan")
	public ResponseEntity<?> deleteUserFavoritePlan(@RequestHeader("accessToken") String header,
			@RequestParam(value = "plan_id") int plan_id) {
		HttpStatus status = HttpStatus.ACCEPTED;

		//log.trace("addUserFavoritePlan : {}",pf);	

		int id = userHelperService.getUserIdFromHeader(header);

		int res = travelPlanService.deleteFavoritePlan(id, plan_id);

		if (res <= 0) {
			throw new RuntimeException();
		}

		status = HttpStatus.NO_CONTENT;

		return ResponseEntity.ok(status);
	}

	@GetMapping("/user-plan")
	@ResponseBody
	public ResponseEntity<List<TravelPlansDto>> userPlan(@RequestHeader("accessToken") String header) {

		int id = userHelperService.getUserIdFromHeader(header);

		List<TravelPlansDto> result = travelPlanService.getUserPlans(id);

		attracionService.setPlanImage(result);

		return ResponseEntity.ok(result);
	}

	@GetMapping("/user-favorite-plans")
	@ResponseBody
	public ResponseEntity<List<TravelPlansDto>> userFavoritePlans(@RequestHeader("accessToken") String header) {

		int id = userHelperService.getUserIdFromHeader(header);

		List<TravelPlansDto> result = travelPlanService.getUserFavoritePlans(id);

		attracionService.setPlanImage(result);

		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/post-comment")
	@ResponseBody
	public ResponseEntity<?> postComment(@RequestHeader("accessToken") String header, @RequestBody TravelPlanCommentsRequestDto req_comment)
	{
		HttpStatus status = HttpStatus.ACCEPTED;
		
		String user_id = jwtUtil.getUserId(header);
		int user_pk = userService.findIdByUserId(user_id).get();
		
		TravelPlanCommentsDto comment = new TravelPlanCommentsDto();

		comment.setPlan_id(req_comment.getPlan_id());
		comment.setUser_id(user_pk);
		comment.setComment(req_comment.getComment());
		comment.setParent_comment_id(req_comment.getParent_comment_id());
		
		int cnt = travelPlanService.postComment(comment);
		
		if(cnt <= 0)
		{
			throw new RuntimeException();
		}
		
		status = HttpStatus.CREATED;
		
		return ResponseEntity.ok(status);
	}
	
	@DeleteMapping("/delete-comment")
	public ResponseEntity<?> deleteComment(@RequestHeader("accessToken") String header, @RequestParam(value="comment_pk") int comment_pk)
	{
		HttpStatus status = HttpStatus.ACCEPTED;

		//본인이 작성한 댓글만 삭제 가능하도록
		int plan_user_pk = travelPlanService.findUserIdByCommentId(comment_pk);
		int user_pk = userHelperService.getUserIdFromHeader(header);
		
		if(plan_user_pk != user_pk) {
			return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
		}
		
		int res = travelPlanService.deleteComment(comment_pk);
		
		if(res < 0) 
		{
			throw new RuntimeException();
		}
		
		status = HttpStatus.NO_CONTENT;
		
		return ResponseEntity.ok(status);	
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
	
	@DeleteMapping("/{plan_id}")
	public ResponseEntity<?> deletePlan(@RequestHeader("accessToken") String header, @PathVariable("plan_id") int plan_id) {
		HttpStatus status = HttpStatus.ACCEPTED;
		
		travelPlanService.delete(plan_id).orElseThrow(() -> new RuntimeException());
		
		status = HttpStatus.NO_CONTENT;
		return new ResponseEntity<>(status);
	}
}
