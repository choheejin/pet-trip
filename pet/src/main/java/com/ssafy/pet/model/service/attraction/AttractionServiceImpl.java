package com.ssafy.pet.model.service.attraction;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.pet.dto.AttractionDetailDto;
import com.ssafy.pet.dto.AttractionsDto;
import com.ssafy.pet.model.mapper.AttractionMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttractionServiceImpl implements AttractionService {
	private final AttractionMapper attractionMapper;

	@Override
	public List<AttractionsDto> searchAttractions(Map<String, Object> params) {
		return attractionMapper.searchAttractions(params);
	}

	@Override
	public int addHotplace(int content_id) {
		return attractionMapper.addHotplace(content_id);
	}

	@Override
	public List<AttractionsDto> viewHotplaces() {
		return attractionMapper.viewHotplaces();
	}

	@Override
	public AttractionDetailDto detailAttraction(int content_id) {
		return attractionMapper.detailAttraction(content_id);
	}
	

}
