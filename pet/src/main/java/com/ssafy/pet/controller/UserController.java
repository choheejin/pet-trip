package com.ssafy.pet.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ssafy.pet.dto.UsersDto;
import com.ssafy.pet.exception.ApplicationException;
import com.ssafy.pet.exception.errorcode.UserErrorCode;
import com.ssafy.pet.model.service.user.UserService;
import com.ssafy.pet.util.JWTUtil;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
	private final UserService userService;
	private final JWTUtil jwtUtil;
	
	@PostMapping("/signup")
	public ResponseEntity<?> userRegister(@RequestBody UsersDto userDto) {
		HttpStatus status = HttpStatus.ACCEPTED;

		System.out.println(userDto);
		
		userService.signup(userDto).orElseThrow(() -> new RuntimeException());
		
		status = HttpStatus.NO_CONTENT;
		
		return new ResponseEntity<>(status);		
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> userLogin(@RequestBody UsersDto user) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;
		
		UsersDto loginUser = userService.login(user).orElseThrow(() -> new ApplicationException(UserErrorCode.UNAUTHORIZED));
		
		String accessToken = jwtUtil.createAccessToken(loginUser.getId(), loginUser.getUser_id());
		
		resultMap.put("access-token", accessToken);
		
		status = HttpStatus.CREATED;
		
		return new ResponseEntity<Map<String, Object>>(resultMap, status);		
	}
	
	@GetMapping("/{user_id}")
	public ResponseEntity<?> userFind(@PathVariable("user_id") String user_id) {
		HttpStatus status = HttpStatus.ACCEPTED;
		
		userService.findIdByUserId(user_id).orElseThrow(() -> new ApplicationException(UserErrorCode.USER_ALREADY_EXISTS));
		
		status = HttpStatus.NO_CONTENT;
		
		return new ResponseEntity<>(status);
	}
	
	@GetMapping("/info")
	public ResponseEntity<?> userInfo(@RequestHeader("accessToken") String header) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;

		String user_id = jwtUtil.getUserId(header);
		UsersDto userInfo = userService.userInfo(user_id).orElseThrow(() -> new ApplicationException(UserErrorCode.UNAUTHORIZED));
		
		resultMap.put("user_id", userInfo.getUser_id());
		resultMap.put("username", userInfo.getUsername());
		resultMap.put("email", userInfo.getEmail());
		
		status = HttpStatus.CREATED;

		return new ResponseEntity<Map<String, Object>>(resultMap, status);		
	}
	
	@PatchMapping("/delete")
	public ResponseEntity<?> userPatch(@RequestHeader("accessToken") String header) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;

		String user_id = jwtUtil.getUserId(header);
		userService.deactivate(user_id).orElseThrow(() -> new ApplicationException(UserErrorCode.UNAUTHORIZED));
		
		status = HttpStatus.NO_CONTENT;
		
		return new ResponseEntity<>(status);	
	}
}
