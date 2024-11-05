package com.example.demo.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Memo {

	public Memo () {
		
	}
	private Long id;
	private String title;
	private String content;
	private LocalDateTime created;
	private LocalDateTime updated;
	private String userName;	//外部キーに対応
	
	
	
}
