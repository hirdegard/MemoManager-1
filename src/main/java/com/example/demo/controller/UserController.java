package com.example.demo.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.domain.Memo;
import com.example.demo.domain.User;
import com.example.demo.mapper.MemoMapper;
import com.example.demo.mapper.UserMapper;

import jakarta.validation.Valid;

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
	
	public String registerUser(@ModelAttribute @Valid User user, Errors errors) {
		if (errors.hasErrors()) {
			//エラー内容
			List<ObjectError> objList = errors.getAllErrors();
			objList.forEach(obj -> System.out.println(obj.toString()));
		}
		//パスワードのエンコード
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userMapper.save(user);
		return "redirect:/login";
		
	}
	

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // login.htmlテンプレートを表示
    }
    
//    @GetMapping("/home")
//    public String showHomePage () {
//    	return "home"; //
//    }
    
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
    
    @PostMapping("/user/memos")
    public String createMemo (@ModelAttribute @Valid Memo memo, Principal principal, Errors errors) {
    	if (errors.hasErrors()) {
			//エラー内容
			List<ObjectError> objList = errors.getAllErrors();
			objList.forEach(obj -> System.out.println(obj.toString()));
		}
    	
    	memo.setUserName(principal.getName());
    	memo.setCreated(LocalDateTime.now());
    	memo.setUpdated(LocalDateTime.now());
    	memoMapper.insert(memo);
    	return "redirect:/user/home";
    }
    
    @PostMapping("/user/memos/{id}/delete")
    public String deleteMemo(@PathVariable Long id, Principal principal) {
    	String username = principal.getName();
    	var memo = memoMapper.findById(id, username);
    	if (memo != null) {
    		memoMapper.delete(id);
    	}
    	return "redirect:/user/home";
    }
    
    @GetMapping("/user/memos/{id}/confirm_delete")
    public String showDeleteConfirm(@PathVariable Long id, Model model, Principal principal) {
    	String username = principal.getName();
    	var memo = memoMapper.findById(id, username);
    	if (memo == null) {
    		return "redirect:/user/home";
    	}
    	model.addAttribute("memo", memo);
    	return "confirmDelete";
    }
    
    @GetMapping("/user/memos/{id}/edit")
    public String showEditMemoForm(@PathVariable Long id, Model model, Principal principal) {
    	String username = principal.getName();
    	var memo = memoMapper.findById(id, username);
    	if (memo == null) {
    		return "redirect:/user/home";
    	}
    	model.addAttribute("memo", memo);
    	return "editMemo";
    }
    
    @PostMapping("/user/memos/{id}/update")
    public String updateMemo(@PathVariable Long id, @ModelAttribute Memo memo, Principal principal) {
    	String username = principal.getName();
    	var existingMemo = memoMapper.findById(id, username);
    	if (existingMemo != null) {
    		existingMemo.setTitle(memo.getTitle());
    		existingMemo.setContent(memo.getContent());
    		existingMemo.setUpdated(LocalDateTime.now());
    		
    		memoMapper.update(existingMemo.getId(), existingMemo.getTitle(), existingMemo.getContent(), existingMemo.getUpdated());
    	}
    	return "redirect:/user/home";
    }
    
    //ユーザーの削除
    @PostMapping("/user/delete")
    public String deleteUserAccouont(Principal principal) {
    	String username = principal.getName();
    	userMapper.deleteUserByUsername(username);
    	return "redirect:/login?logout";
    }
    
    //削除画面への遷移
    @GetMapping("/user/delete/confirm")
    public String showDeleteConfirmPage() {
    	return "deleteConfirm";
    }
}
