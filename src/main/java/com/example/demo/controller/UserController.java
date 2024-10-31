package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.domain.User;
import com.example.demo.mapper.UserMapper;

@Controller
public class UserController {

	@Autowired
	private UserMapper userMapper;
	
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
}
