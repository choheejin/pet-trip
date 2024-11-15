package com.ssafy.pet.controller;

import java.util.ArrayList;
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
import com.ssafy.pet.dto.PetAttractionsDto;
import com.ssafy.pet.exception.ApplicationException;
import com.ssafy.pet.exception.errorcode.SearchErrorCode;
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
	public List<AttractionDetailDto> searchDetailByKeyword(@RequestParam(value = "keyword", required = false) String keyword) {
	    if (keyword == null) {
	        // 기본 content_id에 따른 조회 로직
	        // 적절한 에러 처리나 반환 값을 설정
	    	throw new ApplicationException(SearchErrorCode.NO_RESULTS_FOUND, keyword);
	    } else {
	        
	        List<AttractionDetailDto> result = attractionService.searchDetailByKeyword(keyword);
	        
	        return result;
	    }
	}
	
}
