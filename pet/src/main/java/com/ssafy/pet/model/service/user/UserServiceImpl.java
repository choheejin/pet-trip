package com.ssafy.pet.model.service.user;

import java.util.Optional;

import org.apache.ibatis.binding.BindingException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.pet.dto.ProfileImageDto;
import com.ssafy.pet.dto.UsersDto;
import com.ssafy.pet.exception.ApplicationException;
import com.ssafy.pet.exception.errorcode.UserErrorCode;
import com.ssafy.pet.model.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private JavaMailSender javaMailSender;
	
	@Override
	public Optional<Integer> signup(UsersDto user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		int cnt = userMapper.signup(user);
		
		return cnt > 0 ? Optional.of(cnt) : Optional.empty();
	}

	@Override
	public Optional<UsersDto> userInfo(String userId) {
		UsersDto info = userMapper.userInfo(userId);
		
		return info != null ? Optional.of(info) : Optional.empty();
	}
	
	@Override
	public Optional<UsersDto> login(UsersDto user) {
		// 아이디가 존재하지 않는다면 바로 return
		String encodePw = this.userInfo(user.getUser_id()).orElseThrow(() -> new ApplicationException(UserErrorCode.UNAUTHORIZED)).getPassword();
		
		UsersDto loginUser = null;
		
		System.out.println(user.getPassword());
		if(passwordEncoder.matches(user.getPassword(), encodePw)) {
			user.setPassword(encodePw);
			loginUser = userMapper.login(user);
		}
		
		return loginUser != null ? Optional.of(loginUser) : Optional.empty();
	}

	@Override
	public Optional<Integer> update(UsersDto user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		int cnt = userMapper.update(user);
		return cnt > 0 ? Optional.of(cnt) : Optional.empty();
	}

	@Override
	public Optional<Integer> deactivate(String user_id) {
		int cnt = userMapper.deactivate(user_id);
		
		return cnt != 0 ? Optional.of(cnt) : Optional.empty();
	}

	@Override
	public Optional<Integer> findIdByUserId(String user_id) {
		Integer id = null;
		
		try {			
			id = userMapper.findIdByUserId(user_id);
		} catch(BindingException e) {
			return Optional.empty();
		}
		
		return Optional.ofNullable(id);
	}

	@Override
	public Optional<Integer> updateProfileImage(int user_id, ProfileImageDto profileImageDto) {
		System.out.println("profileImageDto : "+ profileImageDto);
		int cnt = userMapper.updateProfileImage(user_id, profileImageDto);
		System.out.println("updateProfileImage 서비스 후");
		return cnt != 0 ? Optional.of(cnt) : Optional.empty();
	}
	
	@Override
	public Optional<Integer> updatePassword(int user_id, String password) {
		int cnt = userMapper.updatePassword(user_id, password);
		return cnt > 0 ? Optional.of(cnt) : Optional.empty();
	}

	@Override
	public ProfileImageDto getProfileImageByUserId(int user_id) {
		
		return userMapper.getProfileImageByUserId(user_id);
	}

	@Override
	public void sendEmail(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		javaMailSender.send(message);
	}

	@Override
	public UsersDto findUserByUserIdAndEmail(String user_id, String email) {
		return userMapper.findUserByUserIdAndEmail(user_id, email);
	}
}
