package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.mapper.UserMapper;
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final UserMapper userMapper;
	
	public SecurityConfig(UserMapper userMapper) {
			this.userMapper = userMapper;
	}
	
	

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                                 
    	http
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/login", "/register").permitAll() // ログインと登録ページは全員アクセス可能
            .requestMatchers("/user/**").authenticated() // `/user/`で始まるパスは認証が必要
            .anyRequest().authenticated())
        .formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/user/home", true) // ログイン成功後にユーザー専用ページへリダイレクト
            .permitAll())
        .logout(logout -> logout
        		.logoutUrl("/logout") // ログアウト用URL
                .logoutSuccessUrl("/login?logout") // ログアウト後のリダイレクト先
            .permitAll());
    	
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            com.example.demo.domain.User user = userMapper.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }
            return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles("USER")
                .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	
}
