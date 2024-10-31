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
                .requestMatchers("/register").permitAll()  // 登録ページは全員がアクセス可能
                .anyRequest().authenticated())             // それ以外のページは認証が必要
            
            .formLogin(login -> login
                .loginPage("/login") // GETリクエストで表示するカスタムログインページ
                .defaultSuccessUrl("/home", true)
                .permitAll())                              // ログインページは全員がアクセス可能
            .logout(logout -> logout
                .permitAll());                             // ログアウトも全員がアクセス可能

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
