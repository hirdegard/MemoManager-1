package com.example.demo.mapper;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.User;

@Transactional
@SpringBootTest
class UserMapperTest {

	@Autowired
	private UserMapper userMapper;
	@Test
	void testSave() {
		var user = new User();
		user.setUsername("hoge");
		user.setPassword("114514");
		userMapper.save(user);
		var result = userMapper.findByUsername("hoge");
		
		assertThat(result.getUsername(),is(user.getUsername()));
		assertThat(result.getPassword(), is(user.getPassword()));
		
	}

	@Test
	void testFindByUsername() {
		var expected = new User(1L, "taro", "114514");
		
		var user = userMapper.findByUsername("taro");
		assertThat(user, is(expected));
	}
	
	//ユーザー削除のテスト
	@Test
	void testDeleteUserByUsername() {
		var user = new User();
		user.setUsername("alpha");
		user.setPassword("114514");
		userMapper.save(user);
		var recorded = userMapper.findByUsername("alpha");
		assertThat(recorded.getUsername(), is(user.getUsername()));
		assertThat(recorded.getPassword(), is(user.getPassword()));
		
		userMapper.deleteUserByUsername("alpha");
		assertThat(userMapper.findByUsername("alpha"), nullValue());
	}

	
}
/*
void deleteUserByUsername(String username);
*/