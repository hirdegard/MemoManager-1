package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {

	public User() {
		
	}
	private Long id;
	private String username;
	private String password;
}
