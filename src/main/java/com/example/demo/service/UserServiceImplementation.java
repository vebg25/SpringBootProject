package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Service
public class UserServiceImplementation implements UserService {
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	BCryptPasswordEncoder bcryptpasswordencoder;

	@Override
	public User saveUser(User user) {
		String string=bcryptpasswordencoder.encode(user.getPassword());
		user.setRole("Role User");
		user.setPassword(string);
		return userRepo.save(user);
	}
}
