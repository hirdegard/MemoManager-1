package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.User;

@Mapper
public interface UserMapper {

	void save (User user);
	
	User findByUsername (String username);
	
	void deleteUserByUsername(String username);
}
