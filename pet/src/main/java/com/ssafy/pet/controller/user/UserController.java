package com.ssafy.pet.controller.user;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ssafy.pet.dto.UsersDto;
import com.ssafy.pet.model.service.user.UserService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
	//TODO: jwt로 바꾸기
	private final UserService userService;
	
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
	
	
	private ResponseEntity<String> exceptionHandling(Exception e) {
		e.printStackTrace();
//		return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error : " + e.getMessage());
	}
}
