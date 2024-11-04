package com.ssafy.pet.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.pet.dto.UsersDto;

@Mapper
public interface UserMapper {
	// 회원가입
	int signup(UsersDto user);
	
	// 아이디, 패스워드로 조회
	UsersDto login(UsersDto user);
	
	// 정보 수정
	int update(UsersDto user);
	
	// 탈퇴
	int deactivate(UsersDto user);
}
