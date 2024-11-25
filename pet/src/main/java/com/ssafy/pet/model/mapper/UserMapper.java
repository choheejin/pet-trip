package com.ssafy.pet.model.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.pet.dto.ProfileImageDto;
import com.ssafy.pet.dto.UsersDto;

@Mapper
public interface UserMapper {
	// 회원가입
	int signup(UsersDto user);

	// 아이디, 패스워드로 조회
	UsersDto login(UsersDto user);
	
	// 사용자 정보 가져오기
	UsersDto userInfo(String user_id);
	
	// 가입된 아이디인지 확인하기
	Integer findIdByUserId(String user_id);
	
	// 정보 수정: username, email, pwd 한꺼번에 수정
	int update(UsersDto user);
		
	// 탈퇴
	int deactivate(String user_id);
	
	String findUserIdById(int id);
	
	// 프로필 이미지 업데이트
	int updateProfileImage(@Param("user_id") int user_id,@Param("profileImageDto") ProfileImageDto profileImageDto);
	
	// 프로필 이미지 조회
	ProfileImageDto getProfileImageByUserId(int user_id);
}
