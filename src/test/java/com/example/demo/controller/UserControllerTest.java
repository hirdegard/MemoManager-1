package com.example.demo.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.security.Principal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.domain.Memo;
import com.example.demo.domain.User;
import com.example.demo.mapper.MemoMapper;
import com.example.demo.mapper.UserMapper;


@WebMvcTest(UserController.class)
class UserControllerTest {

	
	@Autowired
	private MockMvc mock;
	
	@MockBean
	private UserMapper userMapper;
	
	@MockBean
	private MemoMapper memoMapper;
	
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
	
	@Test
	@WithMockUser
	void testShowCreateMemoForm () throws Exception {
		mock.perform(get("/user/memos/new"))
		.andExpect(status().isOk())
		.andExpect(view().name("createMemo"))
		;
	}
	
	@Test
	@WithMockUser(username="testUser")
	void testCreateMemo () throws Exception {
		
		Principal principal = () -> "testUser";
		var memo = new Memo();
		memo.setTitle("Test Title");
		memo.setContent("Test Content");
		memo.setUserName(principal.getName());
		memo.setCreated(LocalDateTime.now());
		
		
		mock.perform(post("/user/memos")
				.flashAttr("memo", memo)
				.principal(principal)
				.with(csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/user/home"));
		
		verify(memoMapper).insert(memo);
		
	}
	
	@Test
	@WithMockUser(username="testUser")
	void testDeleteMemoNormal () throws Exception {
		//idでレコードを検索して見つかる場合
		Long id = 1L;
		Principal principal = () -> "testUser";
		var memo = new Memo();
		when(memoMapper.findById(id, principal.getName())).thenReturn(memo);
		
		mock.perform(post("/user/memos/{id}/delete", 1)
				.principal(principal)
				.with(csrf()))
				
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/user/home"));
		verify(memoMapper, times(1)).findById(id, principal.getName());
		verify(memoMapper, times(1)).delete(id);
	}
	
	@Test
	@WithMockUser(username="testUser")
	void testDeleteMemoAbnormal () throws Exception {
		//idでレコードを検索して見つからない場合
		Long id = 1L;
		Principal principal = () -> "testUser";
		
		when(memoMapper.findById(id, principal.getName())).thenReturn(null);
		
		mock.perform(post("/user/memos/{id}/delete", 1)
				.principal(principal)
				.with(csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/user/home"));
		
		verify(memoMapper, times(1)).findById(id, principal.getName());
		verify(memoMapper, times(0)).delete(id);
		
	}
	
	@Test
	@WithMockUser(username="testUser")
	void testShowDeleteConfirmNormal() throws Exception {
		//メモが見つかる
		Long id = 1L;
		Principal principal = () -> "testUser";
		var memo = new Memo();
		when(memoMapper.findById(id, principal.getName())).thenReturn(memo);
		
		mock.perform(get("/user/memos/{id}/confirm_delete", 1)
				.principal(principal))
		.andExpect(view().name("confirmDelete"))
		.andExpect(model().attribute("memo", memo));
		
		verify(memoMapper, times(1)).findById(id, principal.getName());
	}
	
	@Test
	@WithMockUser(username="testUser")
	void testShowDeleteConfirmAbnormal() throws Exception {
		//メモが見つかる
		Long id = 1L;
		Principal principal = () -> "testUser";
		
		when(memoMapper.findById(id, principal.getName())).thenReturn(null);
		
		mock.perform(get("/user/memos/{id}/confirm_delete", 1)
				.principal(principal))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/user/home"));
		
		verify(memoMapper, times(1)).findById(id, principal.getName());
	}
	
	
	@Test
	@WithMockUser(username="testUser")
	void testShowEditMemoFormNormal () throws Exception {
		Principal principal = () -> "testUser";
		Long id = 3L;
		var memo = new Memo();
		when(memoMapper.findById(id, principal.getName())).thenReturn(memo);
		
		mock.perform(get("/user/memos/{id}/edit", 3)
				.principal(principal))
		.andExpect(status().isOk())
		.andExpect(model().attribute("memo", memo))
		.andExpect(view().name("editMemo"));
		
		verify(memoMapper, times(1)).findById(id, principal.getName());
	}
	
	@Test
	@WithMockUser(username="testUser")
	void testShowEditMemoFormAbnormal () throws Exception {
		Principal principal = () -> "testUser";
		Long id = 3L;
		when(memoMapper.findById(id, principal.getName())).thenReturn(null);
		
		mock.perform(get("/user/memos/{id}/edit", 3).principal(principal))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/user/home"));
		
		verify(memoMapper, times(1)).findById(id, principal.getName());
	}
	
	
	@Test
	@WithMockUser(username="testUser")
	void testUpdateMemoNormal () throws Exception {
		Principal principal = () -> "testUser";
		Long id = 3L;
		var memo = new Memo();
		memo.setId(id);
		memo.setUserName(principal.getName());
		memo.setTitle("old Title");
		memo.setContent("old Content");
		memo.setUpdated(LocalDateTime.now().minusDays(1));
		when(memoMapper.findById(id, principal.getName())).thenReturn(memo);
		
		
		
		mock.perform(post("/user/memos/{id}/update", 3).principal(principal)
				.param("title", "New Title")
				.param("content", "New Content")
				.with(csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/user/home"));
		
		 ArgumentCaptor<LocalDateTime> updatedTimeCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
	        verify(memoMapper, times(1)).update(eq(id), eq("New Title"), eq("New Content"), updatedTimeCaptor.capture());
	}
	
	@Test
	@WithMockUser(username="testUser")
	void testUpdateMemoAbnormal () throws Exception {
		Principal principal = () -> "testUser";
		Long id = 3L;
		when(memoMapper.findById(id, principal.getName())).thenReturn(null);
		
		mock.perform(post("/user/memos/{id}/update", 3).principal(principal)
				.param("title", "New Title")
				.param("content", "New Content")
				.with(csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/user/home"));
	}
	
}
