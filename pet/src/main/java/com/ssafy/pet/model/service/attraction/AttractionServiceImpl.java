package com.ssafy.pet.model.service.attraction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.pet.config.PaginationConstants;
import com.ssafy.pet.dto.AttractionDetailDto;
import com.ssafy.pet.dto.AttractionsDto;
import com.ssafy.pet.dto.HotplaceDto;
import com.ssafy.pet.dto.PetAttractionsDto;
import com.ssafy.pet.dto.TravelPlansDto;
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

	//searchDetailByKeyword함수를 위한 helper function
	public void addDetails(List<Integer> content_ids, List<AttractionDetailDto> result) {
		for(int content_id : content_ids)
		{
			AttractionDetailDto detail = new AttractionDetailDto();
			detail.setAttraction(attractionMapper.searchByContentID(content_id));
			detail.setPetAttraction(attractionMapper.searchPetByContentID(content_id));
			result.add(detail);
		}
	}
	
	//travel_plan dto의 first_image를 지정해주는 함수
	public void setPlanImage(List<TravelPlansDto> plans)
	{
		for(var plan : plans)
		{
			System.out.println("plan id: " + plan.getId());
			int content_id = attractionMapper.getContentIdByPlanId(plan.getId());
			//System.out.println("content_id: " + content_id);
			String image = attractionMapper.getImageById(content_id);
			//System.out.println("image: " + image);
			plan.setImage(image);
		}
	}
	
	@Override
	public List<AttractionDetailDto> searchDetailByKeyword(String keyword, int page_start, int page_size) { 
		List<Integer> content_ids = attractionMapper.searchDetailByKeyword(keyword, page_start, page_size);

		List<AttractionDetailDto> result = new ArrayList<>();
		
		addDetails(content_ids, result);
		
		return result;
	}

	@Override
	public int searchUserByUserId(String user_id) { 
		return attractionMapper.searchUserByUserId(user_id);
	}

	@Override
	public List<Integer> searchGugunCodeBySidoCode(int sido_code) {
		return attractionMapper.searchGugunCodeBySidoCode(sido_code);
	}

	@Override
	public List<TravelPlansDto> getPlanRanking(int page_start, int page_size) {
		return attractionMapper.getPlanRanking(page_start, page_size);
	}

	@Override
	public List<AttractionsDto> getHotplaceRanking(int page_start, int page_size) {
		return attractionMapper.getHotplaceRanking(page_start, page_size);
	}

	@Override
	public String getImageById(int content_id) {
		return attractionMapper.getImageById(content_id);
	}

	@Override
	public int getContentIdByPlanId(int plan_id) {
		return attractionMapper.getContentIdByPlanId(plan_id);
	}
}