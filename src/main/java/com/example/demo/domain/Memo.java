package com.example.demo.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Memo {

	private Long id;
	private String title;
	private String content;
	private LocalDateTime created;
	private LocalDateTime updated;
	private String userName;
	
	
	
}
