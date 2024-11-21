package com.ssafy.pet.model.service.user;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.pet.dto.ProfileImageDto;
import com.ssafy.pet.dto.UsersDto;

public interface UserService {
	// 회원가입
	Optional<Integer> signup(UsersDto user);
	
	// 아이디, 패스워드로 조회
	Optional<UsersDto> login(UsersDto user);
	
	// 정보 조회
	Optional<UsersDto> userInfo(String user_id);
	
	// 사용자 아이디 이미 사용중인지 확인하기
	Optional<Boolean> findIdByUserId(String user_id);
	
	// 정보 수정
	Optional<Integer> update(UsersDto user);
	
	//TODO:: 비밀번호 수정도 만들면 좋을듯
	
	// 탈퇴
	Optional<Integer> deactivate(String user_id);
	
	// 프로필 이미지 업데이트
	Optional<Integer> updateProfileImage(int user_id, ProfileImageDto profileImageDto);
	
	// 프로필 이미지 조회
	ProfileImageDto getProfileImageByUserId(int user_id);

	
}
