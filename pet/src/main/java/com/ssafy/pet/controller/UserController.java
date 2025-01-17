package com.ssafy.pet.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.pet.dto.PasswordCheck;
import com.ssafy.pet.dto.ProfileImageDto;
import com.ssafy.pet.dto.UsersDto;
import com.ssafy.pet.exception.ApplicationException;
import com.ssafy.pet.exception.errorcode.UserErrorCode;
import com.ssafy.pet.model.service.user.UserService;
import com.ssafy.pet.util.JWTUtil;
import com.ssafy.pet.util.PasswordGenerator;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
	private final JWTUtil jwtUtil;
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;

	@PostMapping("/signup")
	public ResponseEntity<?> userRegister(@RequestBody UsersDto userDto) {
		HttpStatus status = HttpStatus.ACCEPTED;

		System.out.println(userDto);

		userService.signup(userDto).orElseThrow(() -> new RuntimeException());

		status = HttpStatus.NO_CONTENT;

		return new ResponseEntity<>(status);
	}

	@PatchMapping("/update-info")
	@ResponseBody
	public ResponseEntity<?> updateUserInfo(@RequestHeader("accessToken") String header, @RequestBody UsersDto user) {
		HttpStatus status = HttpStatus.ACCEPTED;

		userService.update(user).orElseThrow(() -> new RuntimeException());

		status = HttpStatus.NO_CONTENT;

		return ResponseEntity.ok(status);
	}

	@PostMapping("/login")
	public ResponseEntity<?> userLogin(@RequestBody UsersDto user) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;

		UsersDto loginUser = userService.login(user)
				.orElseThrow(() -> new ApplicationException(UserErrorCode.UNAUTHORIZED));

		if (loginUser.is_temporary_password()) {
			resultMap.put("message", "임시 비밀번호로 로그인되었습니다. 비밀번호를 변경하세요.");
			resultMap.put("resetToken", jwtUtil.createPasswordResetToken(loginUser.getId()));
			status = HttpStatus.FORBIDDEN;
		} else {

			String accessToken = jwtUtil.createAccessToken(loginUser.getUser_id());
			resultMap.put("access-token", accessToken);
			status = HttpStatus.CREATED;
		}

		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@PatchMapping("/forgot-password")
	public ResponseEntity<?> forgetPassword(@RequestBody PasswordCheck pwc) {

		UsersDto user = userService.findUserByUserIdAndEmail(pwc.getUser_id(), pwc.getEmail());
		System.out.println("forgetPassword user: " + user);

		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("등록되지 않은 사용자 입니다.");
		}

		String temporaryPwd = PasswordGenerator.generateSecurePassword();

		userService.updatePassword(user.getId(), passwordEncoder.encode(temporaryPwd), true);

		String resetToken = jwtUtil.createPasswordResetToken(user.getId());

		try {
			userService.sendPwdResetEmail(user.getEmail(), temporaryPwd);
		} catch (MessagingException e) {
			throw new RuntimeException("이메일 전송 중 문제가 발생했습니다.", e);
		}

		// 클라이언트에게 성공 응답 반환
		return ResponseEntity.ok(Map.of("message", "임시 비밀번호가 이메일로 발송되었습니다.", "resetToken", resetToken // 선택: 클라이언트에 토큰
																										// 제공
		));
	}

	@PatchMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestHeader("accessToken") String header,
			@RequestParam("new_password") String new_password) {
		if (!jwtUtil.isPasswordResetToken(header)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
		}

		int user_pk = jwtUtil.getUserPk(header);
		userService.updatePassword(user_pk, passwordEncoder.encode(new_password), false);
		return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다");
	}

	@GetMapping("/{user_id}")
	public ResponseEntity<?> userFind(@PathVariable("user_id") String user_id) {
		return userService.findIdByUserId(user_id).map(id -> {
			// 아이디가 존재할 경우
			System.out.println("userFind id: " + id);
			return new ResponseEntity<>("이미 존재하는 아이디 입니다.", HttpStatus.CONFLICT);
		}).orElseGet(() -> {
			// 아이디가 존재하지 않을 경우
			System.out.println("userFind: No user found with user_id " + user_id);
			return new ResponseEntity<>("사용 가능한 아이디 입니다.", HttpStatus.OK);
		});
	}

	@GetMapping("/info")
	public ResponseEntity<?> userInfo(@RequestHeader("accessToken") String header) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;

		String user_id = jwtUtil.getUserId(header);
		UsersDto userInfo = userService.userInfo(user_id)
				.orElseThrow(() -> new ApplicationException(UserErrorCode.UNAUTHORIZED));

		resultMap.put("pk_id", userInfo.getId());
		resultMap.put("user_id", userInfo.getUser_id());
		resultMap.put("username", userInfo.getUsername());
		resultMap.put("email", userInfo.getEmail());

		ProfileImageDto profileImage = userService.getProfileImageByUserId(user_id);
		// 프로필 이미지 데이터 가져오기
		if (profileImage != null && profileImage.getStored_name() != null) {
			System.out.println("profileImage" + profileImage);
			resultMap.put("image", profileImage.getFile_path() + profileImage.getStored_name());
		} else {
			resultMap.put("image", null);
		}

		status = HttpStatus.CREATED;

		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@PatchMapping("/delete")
	public ResponseEntity<?> userPatch(@RequestHeader("accessToken") String header) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;

		String user_id = jwtUtil.getUserId(header);
		userService.deactivate(user_id).orElseThrow(() -> new ApplicationException(UserErrorCode.UNAUTHORIZED));

		status = HttpStatus.NO_CONTENT;

		return new ResponseEntity<>(status);
	}

	@GetMapping("/profileimage")
	public ResponseEntity<?> getProfileImage(@RequestHeader("accessToken") String header) {
		String user_id = jwtUtil.getUserId(header);

		ProfileImageDto profileImage = userService.getProfileImageByUserId(user_id);

		return new ResponseEntity<>(profileImage, HttpStatus.OK);
	}

	@PatchMapping("/updateimage")
	public ResponseEntity<?> updateProfileImage(@RequestHeader("accessToken") String header,
			@RequestParam("file") MultipartFile file) {
		String user_id = jwtUtil.getUserId(header);

		UsersDto userInfo = userService.userInfo(user_id)
				.orElseThrow(() -> new ApplicationException(UserErrorCode.UNAUTHORIZED));

		// 파일 저장 경로 설정
		String directory = "src/main/resources/upload/profile/";
		String originalFileName = file.getOriginalFilename();
//		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		String extension = ".jpg";

		String fileName = user_id + extension; // 저장될 파일 이름

		System.out.println("파일 도착 : !!!!!!" + fileName);
		File destination = new File(directory + fileName);

		try {
			// 파일 저장
			Files.copy(file.getInputStream(), Paths.get(destination.getAbsolutePath()),
					StandardCopyOption.REPLACE_EXISTING);

			ProfileImageDto img = new ProfileImageDto();
			img.setUser_id(userInfo.getId());
			img.setOriginal_name(file.getOriginalFilename());
			img.setStored_name(fileName);
			img.setFile_path(directory);
			img.setUploaded_at(LocalDateTime.now());

			System.out.println("저장된 이미지 정보 : " + img);

			int cnt = userService.updateProfileImage(user_id, img)
					.orElseThrow(() -> new ApplicationException(UserErrorCode.UNAUTHORIZED));
			System.out.println("업데이트된 이미지 갯수: " + cnt);

			// 프로필 이미지 업데이트가 성공했으면
			if (cnt > 0) {
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				// 업데이트 실패 시
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
