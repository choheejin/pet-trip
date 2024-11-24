package com.ssafy.pet.interceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	// GET 요청이지만 accessToken 확인이 필요한 경우 패턴 추가 필요
	private final List<String> getPatterns = Arrays.asList(
			"/pet/user/info",
			"/pet/plan/user-plan",
			"/pet/attraction/user-likes",
			"/pet/plan/user-favorite-plans"
	);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (request.getMethod().equalsIgnoreCase("OPTIONS"))
			return true;

		if (request.getMethod().equalsIgnoreCase("GET")) {
			String uri = request.getRequestURI().trim();

			System.out.println("get메서드에서 인터셉터 작");

			if (!uri.equalsIgnoreCase("/pet/user/info")) {
				return true;
			}
		}

		String header = request.getHeader("accessToken");

		System.out.println(header);
		System.out.println("인터셉터 작동");
		if (header == null || !jwtUtil.checkToken(header)) {
			throw new ApplicationException(UserErrorCode.UNAUTHORIZED);
		}

		return true;
	}
}
