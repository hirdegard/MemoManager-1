package com.example.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.domain.Memo;
import com.example.demo.domain.User;
import com.example.demo.mapper.MemoMapper;
import com.example.demo.mapper.UserMapper;

@Controller
public class UserController {

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private MemoMapper memoMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}
	
	@PostMapping("/register")
	
	public String registerUser(@ModelAttribute User user) {
		//パスワードのエンコード
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userMapper.save(user);
		return "redirect:/login";
		
	}
	

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // login.htmlテンプレートを表示
    }
    
    @GetMapping("/home")
    public String showHomePage () {
    	return "home"; //
    }
    
    //ユーザのページ
    @GetMapping("/user/home")
    public String userHome(Principal principal, Model model) {
    	
    	if (principal == null) {
    		throw new RuntimeException("ログインユーザーが見つからない");
    	}
    	
    	String username = principal.getName();
    	
    	List<Memo> memos = memoMapper.findAll(username);
    	model.addAttribute("memos", memos);
    	model.addAttribute("username", username);
    	
    	return "userHome";
    }
    
    @GetMapping("/user/memos/new")
    public String showCreateMemoForm(Model model) {
    	model.addAttribute("memo", new Memo());
    	return "createMemo";
    }
}
