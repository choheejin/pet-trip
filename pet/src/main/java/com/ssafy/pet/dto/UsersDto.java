package com.ssafy.pet.dto;

import lombok.Data;

@Data
public class UsersDto {
	private int id;
	private String username;
	private String email;
	private String password;
	private String create_at;
	private boolean is_activate;

}
