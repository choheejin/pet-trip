package com.ssafy.pet.model.service.user;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.pet.dto.UsersDto;
import com.ssafy.pet.exception.UserException;
import com.ssafy.pet.exception.UserExceptionType;
import com.ssafy.pet.model.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	
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
		String encodePw = this.userInfo(user.getUser_id()).orElseThrow(() -> new UserException(UserExceptionType.UN_AUTHORIZED)).getPassword();
		UsersDto loginUser = null;
		
		if(passwordEncoder.matches(user.getPassword(), encodePw)) {
			user.setPassword(encodePw);
			loginUser = userMapper.login(user);
		}
		
		return loginUser != null ? Optional.of(loginUser) : Optional.empty();
	}

	@Override
	public Optional<Integer> update(UsersDto user) {
		return Optional.empty();
	}

	@Override
	public Optional<Integer> deactivate(String user_id) {
		int cnt = userMapper.deactivate(user_id);
		
		return cnt != 0 ? Optional.of(cnt) : Optional.empty();
	}

	@Override
	public Optional<Boolean> findById(String user_id) {
		int cnt = userMapper.findById(user_id);
		
		return cnt == 0 ? Optional.of(true) : Optional.empty();
	}


}
