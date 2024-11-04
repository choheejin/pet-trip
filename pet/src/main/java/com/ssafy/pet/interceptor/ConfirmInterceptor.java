package com.ssafy.pet.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

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
			response.setStatus(401);
			response.setContentType("application/json");
			
			response.getWriter().write("{\"error\": \"Invalid or missing access token\"}");
			response.getWriter().flush();
			return false;
		}
		return true;
	}
}
