package com.example.UserService.Controller.Request;

import lombok.Data;

@Data
public class SignUpRequest 
{
	private Long id;
	private String username;
	private String password;
	private String email;
	
}
