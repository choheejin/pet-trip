package com.ssafy.pet.model.service.travelplan;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.pet.config.PaginationConstants;
import com.ssafy.pet.domain.TravelPlans;
import com.ssafy.pet.dto.ProfileImageDto;
import com.ssafy.pet.dto.TravelPlanCommentsDto;
import com.ssafy.pet.dto.TravelPlanCommentsRequestDto;
import com.ssafy.pet.dto.TravelPlanItemsDto;
import com.ssafy.pet.dto.TravelPlansDto;
import com.ssafy.pet.dto.UsersDto;
import com.ssafy.pet.dto.response.TravelPlansResponseDto;
import com.ssafy.pet.exception.ApplicationException;
import com.ssafy.pet.exception.errorcode.SearchErrorCode;
import com.ssafy.pet.exception.errorcode.TravelPlanErrorCode;
import com.ssafy.pet.exception.errorcode.UserErrorCode;
import com.ssafy.pet.model.mapper.AttractionMapper;
import com.ssafy.pet.model.mapper.TravelPlanMapper;
import com.ssafy.pet.model.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelPlanServiceImpl implements TravelPlanService {
	private final TravelPlanMapper travelPlanMapper;
	private final UserMapper userMapper;
	private final AttractionMapper attractionMapper;

	@Override
	@Transactional
	public Optional<Integer> insert(Map<String, Object> parmas) throws Exception {
		String u_id = (String) parmas.get("u_id");
		TravelPlansDto plan = (TravelPlansDto) parmas.get("plan");
		List<TravelPlanItemsDto> items = (List<TravelPlanItemsDto>) parmas.get("items");
		
		Map<String, Object> planParam = new HashMap<>();
		planParam.put("u_id", u_id);
		planParam.put("plan", plan);
		
		int id = this.insertPlan(planParam).orElseThrow(() -> new Exception());
		
		Map<String, Object> itemParam = new HashMap<>();
		itemParam.put("id", id);
		itemParam.put("items", items);

		int cnt = this.insertItem(itemParam).orElseThrow(() -> new Exception());
		
		return cnt == 0 ? Optional.empty() : Optional.of(cnt);
	}
	
	
	@Override
	public Optional<Integer> insertPlan(Map<String, Object> params) {
		String u_id = (String) params.get("u_id");
		TravelPlansDto plan = (TravelPlansDto) params.get("plan");
		
		Map<String, Object> param = new HashMap<>();
		param.put("user_id", u_id);
		param.put("title", plan.getTitle());
		param.put("is_public", plan.getIs_public());
		param.put("description", plan.getDescription());
		param.put("id", null);
		
		int cnt = travelPlanMapper.insertPlan(param);
		
		return cnt == 0 ? Optional.empty() : Optional.of(Integer.parseInt(param.get("id").toString()));
	}

	@Override
	public Optional<Integer> insertItem(Map<String, Object> params) {
		
		int cnt = travelPlanMapper.insertPlanItem(params);
		
		return cnt == 0 ? Optional.empty() : Optional.of(Integer.parseInt(params.get("id").toString()));
	}


	@Override
	@Transactional
	public Optional<Integer> update(Map<String, Object> params) {
		int plan_id = (Integer) params.get("plan_id");
		int user_id = (Integer) params.get("user_id");
		
		TravelPlansDto plan = (TravelPlansDto) params.get("plan");
		List<TravelPlanItemsDto> items = (List<TravelPlanItemsDto>) params.get("items");
		
		Map<String, Object> param = new HashMap<>();
		param.put("user_id", user_id);
		param.put("plan_id", plan_id);		
		param.put("plan", plan);
		param.put("items", items);
		
		this.updatePlan(param);
		this.updateItem(param);

		return Optional.of(1);
	}

	@Override
	public Optional<Integer> updatePlan(Map<String, Object> params) {
		int user_id = (Integer) params.get("user_id");
		int plan_id = Integer.parseInt(params.get("plan_id").toString());
		TravelPlansDto plan = (TravelPlansDto) params.get("plan");
		
		Map<String, Object> param = new HashMap<>();
		param.put("title", plan.getTitle());
		param.put("is_public", plan.getIs_public());
		param.put("description", plan.getDescription());
		param.put("plan_id", plan_id);
		param.put("user_id", user_id);
		
		int cnt = travelPlanMapper.updatePlan(param);
		
		return cnt == 0 ? Optional.empty() : Optional.of(cnt);
	}

	@Override
	public Optional<Integer> updateItem(Map<String, Object> params) {		
		int cnt = travelPlanMapper.updatePlanItem(params);
		
		return cnt == 0 ? Optional.empty() : Optional.of(cnt);
	}

	@Override
	public TravelPlansDto findPlanByIdAndUserId(int id, String userId) {
		int u_id = userMapper.findIdByUserId(userId); // 사용자의 PK 가져오기
		
		if(travelPlanMapper.findPlanById(id) == null) throw new ApplicationException(TravelPlanErrorCode.NO_CONTENT); // 요청 게시글 존재하지 않음
		
		Map<String, Object> param = new HashMap<>();
		param.put("id", id);
		param.put("user_id", u_id);

		TravelPlansDto plan = travelPlanMapper.findPlanByUserIdAndId(param); // 수정하고자 하는 게시글을 반환
		if(plan == null || u_id != plan.getUser_id()) throw new ApplicationException(TravelPlanErrorCode.UPDATE_FROBIDDEN); // 작성자와 요청한 사람이 다름
		
		return plan;
	}
	
	@Override
	public Optional<Map<String, Object>> findPlanWithItemsById(String user_id, int id) {
		TravelPlansDto plan = travelPlanMapper.findPlanById(id);
		String userId = userMapper.findUserIdById(plan.getUser_id());
		
		if(plan.getIs_public() == 0 && userId.equals(userId)) {
			throw new ApplicationException(UserErrorCode.UNAUTHORIZED);
		}
		
		travelPlanMapper.incrementViewCount(id);
		
		UsersDto userInfo = userMapper.userInfo(userId);
		ProfileImageDto profileInfo = userMapper.getProfileImageByUserId(userInfo.getId());
		List<Map<String, Object>> items = travelPlanMapper.findItemById(id);

		
		Map<String, Object> userMap = new HashMap<>();
		userMap.put("user_id", userInfo.getUser_id());
		if(profileInfo != null) {
			userMap.put("profile_path", profileInfo.getFile_path() + profileInfo.getStored_name());
		}
		
		Map<String, Object> resultMap = new HashMap<>();
		
		resultMap.put("userInfo", userMap);
		resultMap.put("plan", plan);
		resultMap.put("items", items);

		
		return plan == null ? Optional.empty() : Optional.of(resultMap);
	}

	@Override
	public List<TravelPlansDto> getOldestPlans(int page_start, int page_size) {
		return travelPlanMapper.getOldestPlans(page_start, page_size);
	}

	@Override
	public List<TravelPlansDto> getNewestPlans(int page_start, int page_size) {
		return travelPlanMapper.getNewestPlans(page_start, page_size);
	}


	@Override
	public List<TravelPlansDto> getPlansByMostViews(int page_start, int page_size) {
		return travelPlanMapper.getPlansByMostViews(page_start, page_size);
	}

	@Override
	public List<TravelPlanCommentsDto> listParentComments(int plan_id) {
		return travelPlanMapper.listParentComments(plan_id);
	}

	@Override
	public List<TravelPlansDto> getUserPlans(String userId) {
		int user_id = userMapper.findIdByUserId(userId);

		return travelPlanMapper.getUserPlans(user_id);
	}

	@Override
	public List<TravelPlansDto> getUserFavoritePlans(String userId) {
		int user_id = userMapper.findIdByUserId(userId);

		return travelPlanMapper.getUserFavoritePlans(user_id);
	}

	@Override
	public TravelPlansResponseDto getPlans(String userId, String sort, int page_start, int page_size) {		
		TravelPlansResponseDto response = new TravelPlansResponseDto();
		
		TravelPlans plans = new TravelPlans(getPlansBySort(sort, page_start, page_size));		
		List<Boolean> favoriteStatus =  plans.getFavoriteStatus(getUserFavoritePlanIds(userId));
		int totalPage = plans.getTotalNum();
		
		response.setPlans(plans.getTravelPlansDtos());
		response.setFavoritePlans(favoriteStatus);
		response.setTotal_pages(totalPage);
		
		return response;
	}
	
	@Override
	public List<TravelPlansDto> getPlansBySort(String sort, int page_start, int page_size) {
		switch(sort) {
			//오래된 순
			case "oldest":
				return travelPlanMapper.getOldestPlans(page_start, page_size);
			//최신 순
			case "newest":
				return travelPlanMapper.getNewestPlans(page_start, page_size);
			//조회 순
			case "views":
				return travelPlanMapper.getPlansByMostViews(page_start, page_size);
			//좋아요 순
			case "likes":
				return attractionMapper.getPlanRanking(page_start, page_size);				
			default:
				throw new ApplicationException(SearchErrorCode.KEYWORD_MISSING, "잘못된 ?sort 명령어");
		}
	}

	@Override
	public List<Integer> getUserFavoritePlanIds(String userId){
		
		if(userId.isBlank()) {
			return null;
		}
		
		int user_id = userMapper.findIdByUserId(userId);
		
		return travelPlanMapper.getUserFavoritePlanIds(user_id);
	}
	
	@Override
	public boolean[] calculateFavoriteStatus(List<TravelPlansDto> plans, String userId) {
		int user_id = userMapper.findIdByUserId(userId);
		
		List<Integer> plan_ids = travelPlanMapper.getUserFavoritePlanIds(user_id);
		Set<Integer> favoritePlanSet = new HashSet<>(plan_ids);
		
		boolean[] favoritePlans = new boolean[PaginationConstants.PAGE_SIZE];
		
		for(int i = 0; i < plans.size(); i++)
		{
			if(favoritePlanSet.contains(plans.get(i).getId()))
			{
				favoritePlans[i] = true;
			}
		}
		
		return favoritePlans;
	}

	@Override
	public int addFavoritePlan(String userId, int favorite_plan_id) {
		int user_id = userMapper.findIdByUserId(userId);
		
		return travelPlanMapper.addFavoritePlan(user_id, favorite_plan_id);
	}

	@Override
	public int deleteFavoritePlan(String userId, int favorite_plan_id) {
		int user_id = userMapper.findIdByUserId(userId);

		return travelPlanMapper.deleteFavoritePlan(user_id, favorite_plan_id);
	}

	@Override
	public List<TravelPlanCommentsRequestDto> listChildComments(int parent_comment_id) {
		
		List<TravelPlanCommentsDto> comments = travelPlanMapper.listChildComments(parent_comment_id);

		List<TravelPlanCommentsRequestDto> response =  convertToCommentsRequestDto(comments).stream().map(comment -> {
			int parent_user_pk = travelPlanMapper.getUserIdByParentCommentId(comment.getParent_comment_id());
			System.out.println(parent_user_pk);
			String parent_user_id = userMapper.findUserIdById(parent_user_pk);
			comment.setMetioned("@"+parent_user_id);
			return comment;
		}).collect(Collectors.toList());
		
		return response;
	}

	@Override
	public TravelPlanCommentsRequestDto postComment(TravelPlanCommentsDto comment) {
		LocalDateTime now = LocalDateTime.now();
		
		int user_id = userMapper.findIdByUserId(comment.getUserId());

		comment.setUser_id(user_id);
		comment.setCreated_at(now);
		comment.setUpdated_at(now);
		
		
		if(comment.getParent_comment_id() != 0) {
			Integer level = travelPlanMapper.getCommentLevelByParentId(comment.getParent_comment_id());
			if(level == null) {
				level = 0;
			}
			comment.setLevel(level + 1);
		}
		
		int cnt = travelPlanMapper.postComment(comment);
		
		if(cnt <= 0) throw new RuntimeException();
		
		TravelPlanCommentsRequestDto response = convertToCommentRequestDto(comment);
		
		return response;
	}

	@Override
	public int deleteComment(int comment_pk, String userId) {
		
		int plan_user_pk = findUserIdByCommentId(comment_pk);
		int user_pk = userMapper.findIdByUserId(userId);
		
		if(user_pk != plan_user_pk) {
			throw new ApplicationException(UserErrorCode.UNAUTHORIZED);
		}
		
		return travelPlanMapper.deleteComment(comment_pk);
	}
	
	@Override
	public boolean getUserLikedPlan(String user_id, int plan_id) {
		int user_pk = userMapper.findIdByUserId(user_id);
		int cnt = travelPlanMapper.getUserLikedPlan(user_pk, plan_id);
		System.out.println(user_pk + ":" + cnt);
		return cnt != 0;
	}

	@Override
	public int findUserIdByCommentId(int comment_pk) {
		return travelPlanMapper.findUserIdByCommentId(comment_pk);
	}

	@Override
	public Optional<Integer> increasePlanViewCnt(int plan_id) {
		int rowsAffected = travelPlanMapper.increasePlanViewCnt(plan_id);
		return rowsAffected > 0 ? Optional.of(rowsAffected) : Optional.empty();
	}
	
	@Override
	public Optional<Integer> delete(int plan_id) {
		int cnt = travelPlanMapper.deletePlan(plan_id);
		return cnt == 0 ? Optional.empty() : Optional.of(cnt);
	}


	@Override
	public List<TravelPlanCommentsRequestDto> convertToCommentsRequestDto(List<TravelPlanCommentsDto> comments) {
		
	    return comments.stream()
	            .map(comment -> convertToCommentRequestDto(comment))
	            .collect(Collectors.toList());
	}
	
	private TravelPlanCommentsRequestDto convertToCommentRequestDto(TravelPlanCommentsDto comment) {
        TravelPlanCommentsRequestDto requestDto = new TravelPlanCommentsRequestDto();
        requestDto.setId(comment.getId());
        requestDto.setPlan_id(comment.getPlan_id());
        requestDto.setUser_id(userMapper.findUserIdById(comment.getUser_id()));
        requestDto.setComment(comment.getComment());
        requestDto.setCreated_at(comment.getCreated_at());
        requestDto.setUpdated_at(comment.getUpdated_at());
        requestDto.setParent_comment_id(comment.getParent_comment_id());
        requestDto.setLevel(comment.getLevel() == null ? 0 : comment.getLevel());
        return requestDto;
	}
}