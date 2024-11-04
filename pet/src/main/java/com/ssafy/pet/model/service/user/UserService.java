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
	
	// 사용자 아이디 이미 사용중인지 확인하기
	Optional<Boolean> findById(String user_id);
	
	// 정보 수정
	Optional<Integer> update(UsersDto user);
	
	//TODO:: 비밀번호 수정도 만들면 좋을듯
	
	// 탈퇴
	Optional<Integer> deactivate(String user_id);
}
