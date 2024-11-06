package com.ssafy.pet.model.service.travelplan;

import java.awt.Insets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.pet.dto.TravelPlanItemsDto;
import com.ssafy.pet.dto.TravelPlansDto;
import com.ssafy.pet.exception.ApplicationException;
import com.ssafy.pet.exception.errorcode.TravelPlanErrorCode;
import com.ssafy.pet.model.mapper.TravelPlanMapper;
import com.ssafy.pet.model.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelPlanServiceImpl implements TravelPlanService {
	private final TravelPlanMapper travelPlanMapper;
	private final UserMapper userMapper;

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
		
		this.updatePlan(param).orElseThrow(() -> new RuntimeException());
		this.updateItem(param).orElseThrow(() -> new RuntimeException());

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
		int cnt = travelPlanMapper.findPlanById(id); // 요청한 게시글이 존재하는지 확인
		
		if(cnt <= 0) throw new ApplicationException(TravelPlanErrorCode.NO_CONTENT); // 요청 게시글 존재하지 않음
		
		Map<String, Object> param = new HashMap<>();
		param.put("id", id);
		param.put("user_id", u_id);

		TravelPlansDto plan = travelPlanMapper.findPlanByUserIdAndId(param); // 수정하고자 하는 게시글을 반환
		if(plan == null || u_id != plan.getUser_id()) throw new ApplicationException(TravelPlanErrorCode.UPDATE_FROBIDDEN); // 작성자와 요청한 사람이 다름
		
		return plan;
	}
}
