package com.example.demo.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.domain.User;
import com.example.demo.mapper.UserMapper;


@WebMvcTest(UserController.class)
class UserControllerTest {

	
	@Autowired
	private MockMvc mock;
	
	@MockBean
	private UserMapper userMapper;
	
	@MockBean
	private PasswordEncoder passwordEncoder;
	
	@Test
	 @WithMockUser // テスト中に認証されたユーザーとしてアクセス
	void testShowRegistrationForm() throws Exception {
		mock.perform(get("/register"))
		.andExpect(view().name("register"))
		.andExpect(model().attribute("user", instanceOf(User.class)))
		;
	}

	@Test
	@WithMockUser //認証されたユーザーとしてテスト
	void testRegisterUser() throws Exception {
		//ユーザーオブジェクトの作成
		var user = new User();
		user.setUsername("Alice");
		user.setPassword("114514");
		
		//passwordEncoderのモック設定
		when(passwordEncoder.encode("114514")).thenReturn("1919810");
		
		
		mock.perform(post("/register")
				.flashAttr("user", user)
				.with(csrf()))
				
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/login"));
		
		//パスワードエンコードとユーザー保存の確認
		verify(passwordEncoder).encode("114514");
		verify(userMapper).save(user);
	}

}
