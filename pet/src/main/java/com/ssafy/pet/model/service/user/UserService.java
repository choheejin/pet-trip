package com.ssafy.pet.model.service.user;

import java.util.Optional;

import com.ssafy.pet.dto.ProfileImageDto;
import com.ssafy.pet.dto.UsersDto;

import jakarta.mail.MessagingException;

public interface UserService {
	// 회원가입
	Optional<Integer> signup(UsersDto user);
	
	// 아이디, 패스워드로 조회
	Optional<UsersDto> login(UsersDto user);
	
	// 정보 조회
	Optional<UsersDto> userInfo(String user_id);
	
	//사용자 아이디와 이메일로 사용자 찾기
	UsersDto findUserByUserIdAndEmail(String user_id, String email);
	
	// 사용자 아이디 이미 사용중인지 확인하기
	Optional<Integer> findIdByUserId(String user_id);
	
	// 정보 수정: username, email, pwd 한꺼번에 수정
	Optional<Integer> update(UsersDto user);
	
	//비밀번호 변경
	Optional<Integer> updatePassword(int user_id, String password, boolean is_temporary_password);
	
	// 탈퇴
	Optional<Integer> deactivate(String user_id);
	
	// 프로필 이미지 업데이트
	Optional<Integer> updateProfileImage(String user_id, ProfileImageDto profileImageDto);
	
	// 프로필 이미지 조회
	ProfileImageDto getProfileImageByUserId(String user_id);
	
	//이메일 보내기
	void sendEmail(String to, String subject, String text);
	
	//비밀번호 찾는 이메일
	void sendPwdResetEmail(String to, String tmp_pwd) throws MessagingException;
}
