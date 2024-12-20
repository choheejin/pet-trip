package com.ssafy.pet.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.pet.config.PaginationConstants;
import com.ssafy.pet.dto.AttractionDetailDto;
import com.ssafy.pet.dto.AttractionsDto;
import com.ssafy.pet.dto.GugunsDto;
import com.ssafy.pet.dto.PaginatedResponseDto;
import com.ssafy.pet.dto.TravelPlansDto;
import com.ssafy.pet.exception.ApplicationException;
import com.ssafy.pet.exception.errorcode.SearchErrorCode;
import com.ssafy.pet.model.service.attraction.AttractionService;
import com.ssafy.pet.util.JWTUtil;
import com.ssafy.pet.util.UtilClass;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/attraction")
public class AttractionController {
	private final JWTUtil jwtUtil;
	private final AttractionService attractionService;

	@GetMapping("/openapi-detail")
	public ResponseEntity<?> getDetail(@RequestHeader(value = "accessToken", required = false) Optional<String> header,
			@RequestParam(name = "contentId") String contentId,
			@RequestParam(name = "contentTypeId") String contentTypeId) throws Exception {

		String serviceUrl = "vL78GRPrfImFlCB4vf/L5qRjYLfgzJgen1x6P5ZzOSUBXxeZNyFU4TYcd202kPgW9BDQp3gXf1QxFhVBmnMQLA==";

		String baseUrl = "https://apis.data.go.kr/B551011/KorService1/detailIntro1";
		String uri = UriComponentsBuilder.fromHttpUrl(baseUrl).queryParam("MobileOS", "ETC")
				.queryParam("MobileApp", "ssafy").queryParam("serviceKey", serviceUrl).queryParam("_type", "json")
				.queryParam("contentId", contentId).queryParam("contentTypeId", contentTypeId).toUriString();

		URL url = new URL(uri);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod("GET");
		conn.setDoOutput(true);

		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line = null;

		while ((line = br.readLine()) != null) {
			sb.append(line);
		}

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> responseBody = objectMapper.readValue(sb.toString(), Map.class);

		System.out.println(responseBody);
		// 유저가 좋아요를 눌렀는지 확인해야한다.
		if (!header.isEmpty()) {
			String user_id = jwtUtil.getUserId(header.get());
			int content_id = Integer.parseInt(contentId);
			boolean isLiked = attractionService.getUserLikedContent(user_id, content_id);
			Map<String, Object> bodyMap = (Map<String, Object>) ((Map<String, Object>) responseBody.get("response"))
					.get("body");
			bodyMap.put("isLiked", isLiked);
		}

		return ResponseEntity.ok(responseBody);
	}

	// (시도, 구군, 이름, 관광지 타입) 선택적 조회
	@GetMapping("/search")
	@ResponseBody
	public ResponseEntity<PaginatedResponseDto<AttractionsDto>> searchAttractions(
			@RequestParam(required = false) Integer sidoCode, @RequestParam(required = false) Integer gugunCode,
			@RequestParam(required = false) String title, @RequestParam(required = false) Integer contentTypeId,
			@RequestParam(required = false) String keyword,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		Map<String, Object> params = new HashMap<>();
		params.put("sidoCode", sidoCode);
		params.put("gugunCode", gugunCode);
		params.put("title", title);
		params.put("contentTypeId", contentTypeId);
		params.put("keyword", keyword);

		int page_start = UtilClass.caculateOffest(page);
		params.put("page_start", page_start);
		params.put("page_size", PaginationConstants.ATTRACTION_PAGE_SIZE);

		List<AttractionsDto> attractions = attractionService.searchAttractions(params);

		params.put("page_start", -1);
		params.put("page_size", -1);

		List<AttractionsDto> allAttractions = attractionService.searchAttractions(params);

		int total_page = UtilClass.calculateTotalPages(allAttractions.size());

		return ResponseEntity.ok(new PaginatedResponseDto<>(attractions, total_page));
	}

	// content_id로 상세 내용 조회
	@GetMapping("/detail/{content_id}")
	@ResponseBody
	public AttractionDetailDto detailAttraction(@PathVariable("content_id") int content_id) {
		AttractionDetailDto result = new AttractionDetailDto();

		// attractions 정보 추가
		result.setAttraction(attractionService.searchByContentID(content_id));
		// petattractions 정보 추가
		result.setPetAttraction(attractionService.searchPetByContentID(content_id));

		return result;
	}

	@GetMapping("/detail")
	@ResponseBody
	public ResponseEntity<PaginatedResponseDto<AttractionDetailDto>> searchDetailByKeyword(
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "keyword", required = false) String keyword) {
		if (keyword == null) {
			throw new ApplicationException(SearchErrorCode.NO_RESULTS_FOUND, keyword);
		} else {

			int page_start = UtilClass.caculateOffest(page);

			List<AttractionDetailDto> attrDetail = attractionService.searchDetailByKeyword(keyword, page_start,
					PaginationConstants.PAGE_SIZE);

			List<AttractionDetailDto> allAttrDetail = attractionService.searchDetailByKeyword(keyword, -1, -1);

			int total_pages = UtilClass.calculateTotalPages(allAttrDetail.size());

			return ResponseEntity.ok(new PaginatedResponseDto<>(attrDetail, total_pages));
		}
	}

	@PostMapping("/hotplace/{content_id}")
	public ResponseEntity<?> updateHotplace(@RequestHeader("accessToken") String header,
			@PathVariable("content_id") int content_id) {

		// 유저 아이디 테이블의 아이디 받기
		String userId = jwtUtil.getUserId(header);

		// 핫플레이스 테이블에 데이터 추가하기
		int result = attractionService.addHotplace(content_id, userId);

		if (result > 0) {
			return ResponseEntity.ok("핫플레이스 추가 성공");
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("핫플레이스 추가 실패");
		}
	}

	@GetMapping("/search/{sido_code}")
	@ResponseBody
	public ResponseEntity<List<GugunsDto>> searchGugunCode(@PathVariable("sido_code") int sido_code) {
		List<GugunsDto> gugun = attractionService.searchGugunCodeBySidoCode(sido_code);

		if (gugun == null) {
			throw new ApplicationException(SearchErrorCode.KEYWORD_MISSING, (Integer.toString(sido_code)));
		}

		return ResponseEntity.ok(gugun);
	}

	@GetMapping("/plan-ranking")
	@ResponseBody
	public ResponseEntity<List<TravelPlansDto>> getPlanRanking(
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {

		int start = UtilClass.caculateOffest(page);
		List<TravelPlansDto> result = attractionService.getPlanRanking(start, PaginationConstants.PAGE_SIZE);

		if (result == null) {
			throw new ApplicationException(SearchErrorCode.NO_RESULTS_FOUND);
		}

		return ResponseEntity.ok(result);
	}

	@GetMapping("/hotplace-ranking")
	@ResponseBody
	public ResponseEntity<List<AttractionsDto>> getHotplacePlanRanking(
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {

		int start = UtilClass.caculateOffest(page);
		List<AttractionsDto> result = attractionService.getHotplaceRanking(start, PaginationConstants.PAGE_SIZE);

		if (result == null) {
			throw new ApplicationException(SearchErrorCode.NO_RESULTS_FOUND);
		}

		return ResponseEntity.ok(result);
	}

	@GetMapping("/user-likes")
	@ResponseBody
	public ResponseEntity<List<AttractionsDto>> getUserFavorites(@RequestHeader("accessToken") String header) {
		List<AttractionsDto> result = new ArrayList<>();

		String userId = jwtUtil.getUserId(header);

		List<Integer> content_ids = attractionService.listHotplaceContentIdsByUserId(userId);

		for (var content_id : content_ids) {
			result.add(attractionService.searchByContentID(content_id));
		}

		return ResponseEntity.ok(result);
	}

	@PostMapping("/add-user-hotplace")
	public ResponseEntity<?> addUserHotplace(@RequestHeader("accessToken") String header,
			@RequestParam(value = "content_id") int content_id) {
		HttpStatus status = HttpStatus.ACCEPTED;

		String userId = jwtUtil.getUserId(header);
		int cnt = attractionService.addHotplace(content_id, userId);

		if (cnt <= 0) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Data conflict or operation not permitted");
		}

		status = HttpStatus.CREATED;

		return ResponseEntity.ok(status);

	}

	@DeleteMapping("/delete-user-hotplace")
	public ResponseEntity<?> deleteUserHotplace(@RequestHeader("accessToken") String header,
			@RequestParam(value = "content_id") int content_id) {
		HttpStatus status = HttpStatus.ACCEPTED;
		String userId = jwtUtil.getUserId(header);

		int res = attractionService.deleteHotplace(content_id, userId);

		if (res <= 0) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Data conflict or operation not permitted");
		}

		status = HttpStatus.NO_CONTENT;

		return ResponseEntity.ok(status);
	}
}
