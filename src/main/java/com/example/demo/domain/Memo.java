package com.example.demo.domain;

import java.time.LocalDateTime;
import java.util.Objects;

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
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Memo other = (Memo) obj;
		return Objects.equals(content, other.content) && Objects.equals(created, other.created)
				&& Objects.equals(title, other.title) && Objects.equals(updated, other.updated)
				&& Objects.equals(userName, other.userName);
	}
	@Override
	public int hashCode() {
		return Objects.hash(content, created, title, updated, userName);
	}
	
	
}
