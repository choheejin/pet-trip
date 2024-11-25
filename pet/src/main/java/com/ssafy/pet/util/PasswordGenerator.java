package com.ssafy.pet.util;

import java.security.SecureRandom;

public class PasswordGenerator {

	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_+=<>?";
	private static final int PASSWORD_LENGTH = 12;
	
	public static String generateSecurePassword() {
		SecureRandom secureRandom = new SecureRandom();
		StringBuilder pwd = new StringBuilder();
		
		for(int i = 0; i < PASSWORD_LENGTH; i++)
		{
			int randomIdx = secureRandom.nextInt(CHARACTERS.length());
			pwd.append(CHARACTERS.charAt(randomIdx));
		}
		
		return pwd.toString();
	}	  
}
