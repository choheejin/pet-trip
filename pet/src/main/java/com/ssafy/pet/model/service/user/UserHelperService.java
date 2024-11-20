package com.ssafy.pet.model.service.user;

import org.springframework.stereotype.Service;

import com.ssafy.pet.dto.UsersDto;
import com.ssafy.pet.exception.ApplicationException;
import com.ssafy.pet.exception.errorcode.UserErrorCode;
import com.ssafy.pet.util.JWTUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserHelperService {
	private final JWTUtil jwtUtil;
	private final UserService userService;
	
	public int getUserIdFromHeader(String header)
	{
		String user_id = jwtUtil.getUserId(header);
		
		if(user_id == null || user_id.isEmpty())
		{
			throw new ApplicationException(UserErrorCode.UNAUTHORIZED);
		}
		
		UsersDto user = userService.userInfo(user_id).orElseThrow(() -> new ApplicationException(UserErrorCode.UNAUTHORIZED));
	
		return user.getId();
	}
}