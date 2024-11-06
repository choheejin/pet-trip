package com.ssafy.pet.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssafy.pet.dto.AttractionDetailDto;
import com.ssafy.pet.dto.AttractionsDto;
import com.ssafy.pet.model.service.attraction.AttractionService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/attraction")
public class AttractionController {
	private final AttractionService attractionService;
	
	// (시도, 구군, 이름, 관광지 타입) 선택적 조회
	@GetMapping("/search")
	@ResponseBody
	public List<AttractionsDto> searchAttractions(
			@RequestParam(required = false) Integer sidoCode,
	        @RequestParam(required = false) Integer gugunCode,
	        @RequestParam(required = false) String title,
	        @RequestParam(required = false) Integer contentTypeId
	){
		Map<String, Object> params = new HashMap<>();
		params.put("sidoCode", sidoCode);
		params.put("gugunCode", gugunCode);
		params.put("title", title);
		params.put("contentTypeId", contentTypeId);
		
		
		return attractionService.searchAttractions(params);
	}
	// content_id로 상세 내용 조회
	@GetMapping("/detail/{id}")
	public AttractionDetailDto detailAttraction(@PathVariable("content_id") int content_id) {
		AttractionDetailDto result = new AttractionDetailDto();
//		
//		result.setAttraction(attractionService.);
		
		return null;
	}
	
	
}
