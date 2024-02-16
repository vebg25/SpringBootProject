package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.OAuth2config.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public UserDetailsService getDetailsService() {
		return new CustomUserDetailService();
	}
	@Bean
	public DaoAuthenticationProvider getAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(getDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}
	@Autowired
	private CustomOAuth2UserService customoauthuserservice;
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http ) throws Exception{
		http.csrf().disable()
		.authorizeHttpRequests().requestMatchers("/register","/login","/saveUser","/home","images/**").
		permitAll().requestMatchers("/user/profile","/user/product/add","/user/product/productDisplay","/showProducts","/update/**",
				"/delete/**","/edit/**","/card/**","/cart","/cart/add/**","/cardlayout","/checkout","/updateProducts","/contactUs","/about").authenticated()
		.and().formLogin().loginPage("/login").loginProcessingUrl("/userLogin")
		.defaultSuccessUrl("/user/profile").permitAll();
		
		return http.build();
	}
}
