package com.ssafy.pet.model.service.user;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
	public Optional<Boolean> findIdByUserId(String user_id) {
		int cnt = 0;
		
		try {			
			cnt = userMapper.findIdByUserId(user_id);
		} catch(BindingException e) {
			return Optional.of(true);
		}
		
		return cnt == 0 ? Optional.of(true) : Optional.empty();
	}

	private String uploadPath = "../uploadimg";
	
	@Override
	public Optional<Integer> updateImage(UsersDto user, MultipartFile image) {
		String imgString = saveImage(image);
		
		user.setImage(imgString);
		int cnt = userMapper.updateImage(user);

		return cnt != 0 ? Optional.of(cnt) : Optional.empty();
	}
	private String saveImage(MultipartFile image) {
	    try {
	    	// 절대경로??? 
	        String imgName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
	        String imgPath = uploadPath + "/" + imgName;
	        
	        // 파일 저장
	        File file = new File(imgPath);
	        image.transferTo(file);
	        return imgPath;
	    } catch (IOException e) {
	        // 예외 처리 로직 (로깅, 사용자에게 알림 등)
	        e.printStackTrace();
	        return null;  // 예외 발생 시 null 반환 (혹은 다른 방식으로 처리)
	    }
	}


}
