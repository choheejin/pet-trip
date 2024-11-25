package com.ssafy.pet.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ssafy.pet.exception.ApplicationException;
import com.ssafy.pet.exception.errorcode.UserErrorCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {
	
	@Value("${jwt.salt}")
	private String salt;

	@Value("${jwt.access-token.expiretime}")
	private long accessTokenExpireTime;

	public String createAccessToken(int i, String user_id) {
		return create(i, user_id, "access-token", accessTokenExpireTime);
	}

	private String create(int id, String user_id, String subject, long expireTime) {
		Claims claims = Jwts.claims().setSubject(subject) // 토큰 제목 설정 ex) access-token, refresh-token
				.setIssuedAt(new Date()) // 생성일 설정
//				만료일 설정 (유효기간)
				.setExpiration(new Date(System.currentTimeMillis() + expireTime));

		System.out.println(claims);

//		저장할 data의 key, value
		claims.put("user_id", user_id);
		claims.put("id", id);

		String jwt = Jwts.builder()
//			Header 설정 : 토큰의 타입, 해쉬 알고리즘 정보 세팅.
				.setHeaderParam(Header.TYPE, Header.JWT_TYPE).setClaims(claims)
//			Signature 설정 : secret key를 활용한 암호화.
				.signWith(SignatureAlgorithm.HS256, this.generateKey()).compact(); // 직렬화 처리.

		return jwt;

	}

//	Signature 설정에 들어갈 key 생성.
	private byte[] generateKey() {
		byte[] key = null;
		try {
//			charset 설정 안하면 사용자 플랫폼의 기본 인코딩 설정으로 인코딩 됨.
			key = salt.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return key;
	}

//	전달 받은 토큰이 제대로 생성된 것인지 확인 하고 문제가 있다면 UnauthorizedException 발생.
	public boolean checkToken(String token) {
		try {
//			Json Web Signature? 서버에서 인증을 근거로 인증 정보를 서버의 private key 서명 한것을 토큰화 한것
//			setSigningKey : JWS 서명 검증을 위한  secret key 세팅
//			parseClaimsJws : 파싱하여 원본 jws 만들기
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(this.generateKey()).build().parseClaimsJws(token);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String getUserId(String authorization) {
		Jws<Claims> claims = null;
		
		try {
			claims = Jwts.parserBuilder().setSigningKey(this.generateKey()).build().parseClaimsJws(authorization);
		} catch (ExpiredJwtException e) {
			// TODO: handle exception
			throw new ApplicationException(UserErrorCode.EXPIRED_JWT);
		}
		
		Map<String, Object> value = claims.getBody();
		return (String) value.get("user_id");
	}
	
	public int getUserPk(String authorization) {
		Jws<Claims> claims = null;
		
		try {
			claims = Jwts.parserBuilder().setSigningKey(this.generateKey()).build().parseClaimsJws(authorization);
		} catch(ExpiredJwtException e) {
			throw new ApplicationException(UserErrorCode.EXPIRED_JWT);
		}
		
		Map<String, Object> value = claims.getBody();
		
		Object userPK = value.get("id");
		
		if(userPK == null) {
			throw new ApplicationException(UserErrorCode.UNAUTHORIZED);
		}
		
		return (int) userPK;
	}
	
	public String createPasswordResetToken(int user_id) {
		Claims claims = Jwts.claims().setSubject("password-reset");
		claims.put("user_id", user_id);
		claims.setIssuedAt(new Date());
		claims.setExpiration(new Date(System.currentTimeMillis() + 3600000));
		
		return Jwts.builder()
				.setClaims(claims)
				.signWith(SignatureAlgorithm.HS256, salt)
				.compact();
	}
}
