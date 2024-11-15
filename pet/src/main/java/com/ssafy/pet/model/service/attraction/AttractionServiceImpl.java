package com.ssafy.pet.model.service.attraction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.pet.dto.AttractionDetailDto;
import com.ssafy.pet.dto.AttractionsDto;
import com.ssafy.pet.dto.HotplaceDto;
import com.ssafy.pet.dto.PetAttractionsDto;
import com.ssafy.pet.dto.UsersDto;
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
	public AttractionsDto searchByContentID(int content_id) {
		return attractionMapper.searchByContentID(content_id);
	}

	@Override
	public PetAttractionsDto searchPetByContentID(int content_id) {
		return attractionMapper.searchPetByContentID(content_id);
	}
	
	@Override
	public AttractionDetailDto detailAttraction(int content_id) {
		return attractionMapper.detailAttraction(content_id);
	}
	
	@Override
	public int addHotplace(int content_id, int user_id) {
		return attractionMapper.addHotplace(content_id, user_id);
	}
	
	@Override
	public HotplaceDto searchHotplaceById(int content_id) {
		return attractionMapper.searchHotplaceById(content_id);
	}

	@Override
	public List<AttractionsDto> viewHotplaces() {
		return attractionMapper.viewHotplaces();
	}

	@Override
	public List<AttractionDetailDto> searchDetailByKeyword(String keyword) { 
		List<Integer> content_ids = attractionMapper.searchDetailByKeyword(keyword);
		List<AttractionDetailDto> result = new ArrayList<>();
		
		for(int i = 0, size = content_ids.size(); i < size; i++)
		{
			AttractionDetailDto detail = new AttractionDetailDto();
			detail.setAttraction(attractionMapper.searchByContentID(content_ids.get(i)));
			detail.setPetAttraction(attractionMapper.searchPetByContentID(content_ids.get(i)));
			result.add(detail);
		}
		
		return result;
	}

	@Override
	public int searchUserByUserId(String user_id) { 
		return attractionMapper.searchUserByUserId(user_id);
	}
}
