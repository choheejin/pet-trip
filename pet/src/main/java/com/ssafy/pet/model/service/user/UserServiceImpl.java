package com.ssafy.pet.model.service.user;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.pet.dto.UsersDto;
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
	public UsersDto login(UsersDto user) {
		// TODO Auto-generated method stub
		return userMapper.login(user);
	}

	@Override
	public int update(UsersDto user) {
		// TODO Auto-generated method stub
		return userMapper.update(user);
	}

	@Override
	public int deactivate(UsersDto user) {
		// TODO Auto-generated method stub
		return userMapper.deactivate(user);
	}

}
