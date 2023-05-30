package com.ensah.smartcontact.services;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ensah.smartcontact.dao.UserRepository;
import com.ensah.smartcontact.models.Role;
import com.ensah.smartcontact.models.User;
import com.ensah.smartcontact.web.dto.SignUpDto;

@Service
public class UserServiceImp implements UserService {

	@Autowired
	UserRepository userRepository;

	
	BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


	@Override
	public User save(SignUpDto signUpDto) {

		User user = new User(signUpDto.getFirstname(), signUpDto.getLastname(), true,
				bCryptPasswordEncoder.encode(signUpDto.getPassword()), signUpDto.getEmail(), signUpDto.getUsername(),
				Arrays.asList(new Role("USER")));

		return userRepository.save(user);
	}

	

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}
	
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsernameOrEmail(username, username);
	
		if (user == null) {
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				mapRoleToAuthorities(user.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRoleToAuthorities(Collection<Role> roles) {

		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());
	}
	

	
	@Override
	public User findUserByUsernameOrEmail(String username, String email) {
		return this.userRepository.findByUsernameOrEmail(username, email);
	}


}
