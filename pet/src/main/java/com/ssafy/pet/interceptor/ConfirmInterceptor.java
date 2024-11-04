package com.ssafy.pet.interceptor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.pet.exception.user.UserException;
import com.ssafy.pet.exception.user.UserExceptionType;
import com.ssafy.pet.util.JWTUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConfirmInterceptor implements HandlerInterceptor {
	private final JWTUtil jwtUtil;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String header = request.getHeader("accessToken");
		
		System.out.println(header);
		System.out.println(jwtUtil.checkToken(header));
		
		if(!jwtUtil.checkToken(header)) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			UserException userException = new UserException(UserExceptionType.UN_AUTHORIZED);
			ObjectMapper objectMapper = new ObjectMapper();

			response.setStatus(userException.getType().getStatus().value());
			response.setContentType("application/json; charset=UTF-8");

			resultMap.put("error", userException.getMessage());

			String jsonResponse = objectMapper.writeValueAsString(resultMap);
			response.getWriter().write(jsonResponse);
			response.getWriter().flush();
			return false;
		}
		return true;
	}
}
