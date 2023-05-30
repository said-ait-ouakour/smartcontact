package com.ensah.smartcontact.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ensah.smartcontact.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	public User findByUsername(String username);
	
	public User findByEmail(String email);
	
	public User findByUsernameOrEmail(String username, String email);
}
