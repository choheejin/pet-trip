package com.ssafy.pet.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	public List<Boolean> getFavoriteStatus(List<Integer> plan_ids) {
		if (plan_ids == null) {
			return new ArrayList<>(Collections.nCopies(getTotalNum(), false));
		}

		Set<Integer> favoritePlanSet = new HashSet<>(plan_ids);

		return plans.stream()
				.map(p -> favoritePlanSet.contains(p.getId()))
				.toList();
	}
}
