package com.ssafy.pet.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ssafy.pet.exception.ApplicationException;
import com.ssafy.pet.exception.errorcode.UserErrorCode;
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
		if(request.getMethod().equals("GET") || request.getMethod().equalsIgnoreCase("OPTIONS")) return true;
		
		String header = request.getHeader("accessToken");
		
		System.out.println(header);

		if (header == null || !jwtUtil.checkToken(header)) {
			throw new ApplicationException(UserErrorCode.UNAUTHORIZED);			
		}
		return true;
	}
}
