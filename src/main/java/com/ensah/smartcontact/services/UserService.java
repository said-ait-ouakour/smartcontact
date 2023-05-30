package com.ensah.smartcontact.services;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.ensah.smartcontact.models.User;
import com.ensah.smartcontact.web.dto.SignUpDto;

public interface UserService extends UserDetailsService {

	User save(SignUpDto signUpDto);

	User save(User user);
	
	User findUserByUsernameOrEmail(String username, String email);

}
