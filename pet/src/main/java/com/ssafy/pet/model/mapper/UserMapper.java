package com.ssafy.pet.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.pet.dto.UsersDto;

@Mapper
public interface UserMapper {
	// 회원가입
	int signup(UsersDto user);

	// 아이디, 패스워드로 조회
	UsersDto login(UsersDto user);
	
	// 사용자 정보 가져오기
	UsersDto userInfo(String user_id);
	
	// 정보 수정
	int update(UsersDto user);
	
	// 탈퇴
	int deactivate(UsersDto user);
}
