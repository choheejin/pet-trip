package com.ssafy.pet.model.service.user;

import java.util.Optional;

import com.ssafy.pet.dto.UsersDto;

public interface UserService {
	// 회원가입
	Optional<Integer> signup(UsersDto user);
	
	// 아이디, 패스워드로 조회
	Optional<UsersDto> login(UsersDto user);
	
	// 정보 조회
	Optional<UsersDto> userInfo(String user_id);
	
	// 정보 수정
	int update(UsersDto user);
	
	//TODO:: 비밀번호 수정도 만들면 좋을듯
	
	// 탈퇴
	int deactivate(UsersDto user);
}
