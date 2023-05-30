package com.ensah.smartcontact.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ensah.smartcontact.models.ContactGroup;
import com.ensah.smartcontact.models.User;

public interface ContactGroupRepository extends JpaRepository<ContactGroup, Long> {

//	get by user 
	List<ContactGroup> findByUser(User user);
	
	ContactGroup findByIdAndUser(Long id, User user) ;
}
