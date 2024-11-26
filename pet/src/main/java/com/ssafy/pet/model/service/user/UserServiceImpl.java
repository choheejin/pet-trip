package com.ssafy.pet.model.service.user;

import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.binding.BindingException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.pet.dto.ProfileImageDto;
import com.ssafy.pet.dto.UsersDto;
import com.ssafy.pet.exception.ApplicationException;
import com.ssafy.pet.exception.errorcode.UserErrorCode;
import com.ssafy.pet.model.mapper.UserMapper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private final JavaMailSender javaMailSender;

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
		String encodePw = this.userInfo(user.getUser_id())
				.orElseThrow(() -> new ApplicationException(UserErrorCode.UNAUTHORIZED)).getPassword();

		UsersDto loginUser = null;

		System.out.println(user.getPassword());
		if (passwordEncoder.matches(user.getPassword(), encodePw)) {
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
		} catch (BindingException e) {
			return Optional.empty();
		}

		return Optional.ofNullable(id);
	}

	@Override
	public Optional<Integer> updateProfileImage(int user_id, ProfileImageDto profileImageDto) {
		System.out.println("profileImageDto : " + profileImageDto);
		int cnt = userMapper.updateProfileImage(user_id, profileImageDto);
		System.out.println("updateProfileImage 서비스 후");
		return cnt != 0 ? Optional.of(cnt) : Optional.empty();
	}

	@Override
	public Optional<Integer> updatePassword(int user_id, String password, boolean is_temporary_password) {
		int cnt = userMapper.updatePassword(user_id, password, is_temporary_password);
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
	public void sendPwdResetEmail(String to, String tmp_pwd) throws MessagingException {
		String subject = "임시 비밀번호 안내드립니다.";
		String htmlContent = String.format("<p>안녕하세요, 고객님.</p>" + "<p>요청하신 <b>임시 비밀번호</b>를 안내드립니다. </p>"
				+ "<p>아래의 임시 비밀번호를 사용하여 로그인 후, 반드시 비밀번호를 변경하시기 바랍니다.</p>"
				+ "<p><b style='color:blue;'>임시 비밀번호: %s</b></p>" + "<p>안전한 서비스 이용을 위해 비밀번호는 주기적으로 변경해 주세요.</p>"
				+ "<br>" + "<p>감사합니다.<br><b>[Pet Trip]</b> 드림</p>", tmp_pwd);

		// 이메일 생성
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

		helper.setTo(to); // 수신자 설정
		helper.setSubject(subject); // 제목 설정
		helper.setText(htmlContent, true); // true: HTML로 보냄

		javaMailSender.send(mimeMessage); // 이메일 전송
	}

	@Override
	public UsersDto findUserByUserIdAndEmail(String user_id, String email) {
		return userMapper.findUserByUserIdAndEmail(user_id, email);
	}
}
