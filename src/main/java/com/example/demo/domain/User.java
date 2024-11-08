package com.example.demo.domain;



import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {

	public User() {
		
	}
	private Long id;
	
	@NotBlank
	private String username;
	
	private String password;
	
}
