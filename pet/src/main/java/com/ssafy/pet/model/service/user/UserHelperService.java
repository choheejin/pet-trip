package com.ssafy.pet.model.service.user;

import org.springframework.stereotype.Service;

import com.ssafy.pet.exception.ApplicationException;
import com.ssafy.pet.exception.errorcode.UserErrorCode;
import com.ssafy.pet.model.service.attraction.AttractionService;
import com.ssafy.pet.util.JWTUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserHelperService {
	private final JWTUtil jwtUtil;
	private final AttractionService attractionService;
	
	public int getUserIdFromHeader(String header)
	{
		String user_id = jwtUtil.getUserId(header);
		
		if(user_id == null || user_id.isEmpty())
		{
			throw new ApplicationException(UserErrorCode.UNAUTHORIZED);
		}
		
		return attractionService.searchUserByUserId(user_id);
	}
}