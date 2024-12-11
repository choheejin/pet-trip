package com.ssafy.pet.domain;

import java.util.Collections;
import java.util.List;

import com.ssafy.pet.dto.TravelPlansDto;

import lombok.ToString;

@ToString
public class TravelPlans {
	private final List<TravelPlansDto> plans;

	public TravelPlans(List<TravelPlansDto> plans) {
		this.plans = plans;
	}

	public List<TravelPlansDto> getTravelPlansDtos() {
		return Collections.unmodifiableList(plans);
	}

	// 총 갯수 구하기
	public int getTotalNum() {
		return plans.size();
	}
		
	// 좋아요 상태 반환하기
	public List<Boolean> getFavoriteStatus() {
		System.out.println(plans);
		return plans.stream()
				.map(p -> {
					System.out.println(p.getLike_user_id());
					return p.getLike_user_id() != -1;
				})
				.toList();
	}
}
