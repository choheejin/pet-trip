package com.ssafy.pet.controller.user;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ssafy.pet.dto.UsersDto;
import com.ssafy.pet.exception.UnAuthorizedException;
import com.ssafy.pet.model.service.user.UserService;
import com.ssafy.pet.util.JWTUtil;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
	//TODO: jwt로 바꾸기
	private final UserService userService;
	private final JWTUtil jwtUtil;
	
	@PostMapping("/signup")
	public ResponseEntity<?> userRegister(@RequestBody UsersDto userDto) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

		try {
			System.out.println(userDto);
			userService.signup(userDto).orElseThrow(() -> new Exception("로그인 중 에러 발생"));
			
			return ResponseEntity.ok().headers(headers).body("성공");
		} catch (Exception e) {
			exceptionHandling(e);
		}
		
		return null;
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> userLogin(@RequestBody UsersDto user) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;
		
		try {
			UsersDto loginUser = userService.login(user).orElseThrow(() -> new UnAuthorizedException());
		
			String accessToken = jwtUtil.createAccessToken(loginUser.getUser_id());
			
			resultMap.put("access-token", accessToken);
			
			status = HttpStatus.CREATED;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(e instanceof UnAuthorizedException) status = HttpStatus.UNAUTHORIZED;
			else status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<Map<String, Object>>(resultMap, status);		
	}
	
	@GetMapping("/info/{user_id}")
	public ResponseEntity<?> userInfo(@RequestHeader("accessToken") String header, @PathVariable("user_id") String user_id) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;

		try {
			UsersDto userInfo = userService.userInfo(user_id).orElseThrow(() -> new Exception());
			
			resultMap.put("user_id", userInfo.getUser_id());
			resultMap.put("username", userInfo.getUsername());
			resultMap.put("email", userInfo.getEmail());
			
			status = HttpStatus.CREATED;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		
		return null;
	}
	
	
	private ResponseEntity<String> exceptionHandling(Exception e) {
		e.printStackTrace();
//		return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error : " + e.getMessage());
	}
}
