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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ssafy.pet.dto.UsersDto;
import com.ssafy.pet.exception.UnAuthorizedException;
import com.ssafy.pet.exception.UserAlreadyExistsException;
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

		try {			
			System.out.println(userDto);
			
			userService.signup(userDto).orElseThrow(() -> new RuntimeException("로그인 중 에러 발생"));
			
			status = HttpStatus.NO_CONTENT;	
		} catch (Exception e) {
			return exceptionHandling(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(status);		
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
			return exceptionHandling(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Map<String, Object>>(resultMap, status);		
	}
	
	@GetMapping("/{user_id}")
	public ResponseEntity<?> userFind(@PathVariable("user_id") String user_id){
		HttpStatus status = HttpStatus.ACCEPTED;
		
		try {
			boolean is_validate = userService.findById(user_id).orElseThrow(() -> new UserAlreadyExistsException());
			
			status = HttpStatus.NO_CONTENT;
		} catch (Exception e) {
			if(e instanceof UserAlreadyExistsException) status = HttpStatus.CONFLICT;
			else status = HttpStatus.INTERNAL_SERVER_ERROR;
			
			return exceptionHandling(e, status);
		}
		
		return new ResponseEntity<>(status);
	}
	
	@GetMapping("/protected/info/{user_id}")
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
			return exceptionHandling(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		
		return new ResponseEntity<Map<String, Object>>(resultMap, status);		
	}
	
	@PatchMapping("/protected/{user_id}")
	public ResponseEntity<?> userPatch(@RequestHeader("accessToken") String header, @PathVariable("user_id") String user_id) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;

		try {
			userService.deactivate(user_id).orElseThrow(() -> new UnAuthorizedException());
			
			status = HttpStatus.NO_CONTENT;
		} catch (Exception e) {
			return exceptionHandling(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(status);	
	}
	
	
	private ResponseEntity<Map<String, Object>> exceptionHandling(Exception e, HttpStatus status) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		e.printStackTrace();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
		
		resultMap.put("error", e.getMessage());
		return ResponseEntity.status(status).body(resultMap);
	}
}
