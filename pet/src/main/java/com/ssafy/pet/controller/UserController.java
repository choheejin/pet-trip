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

import com.ssafy.pet.dto.ProfileImageDto;
import com.ssafy.pet.dto.UsersDto;
import com.ssafy.pet.exception.ApplicationException;
import com.ssafy.pet.exception.errorcode.UserErrorCode;
import com.ssafy.pet.model.service.user.UserHelperService;
import com.ssafy.pet.model.service.user.UserService;
import com.ssafy.pet.util.JWTUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
	private final JWTUtil jwtUtil;
	private final UserService userService;
	private final UserHelperService userHelperService;

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
		
		UsersDto loginUser = userService.login(user).orElseThrow(() -> new ApplicationException(UserErrorCode.UNAUTHORIZED));

		String accessToken = jwtUtil.createAccessToken(loginUser.getId(), loginUser.getUser_id());

		resultMap.put("access-token", accessToken);

		status = HttpStatus.CREATED;

		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@GetMapping("/{user_id}")
	public ResponseEntity<?> userFind(@PathVariable("user_id") String user_id) {
		HttpStatus status = HttpStatus.ACCEPTED;

		userService.findIdByUserId(user_id)
				.orElseThrow(() -> new ApplicationException(UserErrorCode.USER_ALREADY_EXISTS));

		status = HttpStatus.NO_CONTENT;

		return new ResponseEntity<>(status);
	}

	@GetMapping("/info")
	public ResponseEntity<?> userInfo(@RequestHeader("accessToken") String header) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;

		String user_id = jwtUtil.getUserId(header);
		UsersDto userInfo = userService.userInfo(user_id)
				.orElseThrow(() -> new ApplicationException(UserErrorCode.UNAUTHORIZED));

		resultMap.put("user_id", userInfo.getUser_id());
		resultMap.put("username", userInfo.getUsername());
		resultMap.put("email", userInfo.getEmail());

		ProfileImageDto profileImage = userService.getProfileImageByUserId(userInfo.getId());
		// 프로필 이미지 데이터 가져오기
		if (profileImage.getStored_name() != null) {
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
		int user_id = userHelperService.getUserIdFromHeader(header);

		ProfileImageDto profileImage = userService.getProfileImageByUserId(user_id);

		return new ResponseEntity<>(profileImage, HttpStatus.OK);
	}

	@PatchMapping("/updateimage")
	public ResponseEntity<?> updateProfileImage(@RequestHeader("accessToken") String header,
			@RequestParam("file") MultipartFile file) {
		String user_id = jwtUtil.getUserId(header);
		int id = userHelperService.getUserIdFromHeader(header);

		UsersDto userInfo = userService.userInfo(user_id)
				.orElseThrow(() -> new ApplicationException(UserErrorCode.UNAUTHORIZED));

		// 파일 저장 경로 설정
		String directory = "src/main/resources/upload/profile/";
		String originalFileName = file.getOriginalFilename();
//		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		String extension = ".jpg";
		
		String fileName = user_id + extension; // 저장될 파일 이름

		System.out.println("파일 도착 : !!!!!!"+fileName);
		File destination = new File(directory + fileName);

		try {
			// 파일 저장
			Files.copy(file.getInputStream(), Paths.get(destination.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);

			ProfileImageDto img = new ProfileImageDto();
			img.setUser_id(userInfo.getId());
			img.setOriginal_name(file.getOriginalFilename());
			img.setStored_name(fileName);
			img.setFile_path(directory);
			img.setUploaded_at(LocalDateTime.now());

			System.out.println("저장된 이미지 정보 : " + img);

			int cnt = userService.updateProfileImage(id, img)
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
