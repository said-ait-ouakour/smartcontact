package com.ensah.smartcontact.services;

import java.util.List;

import com.ensah.smartcontact.models.ContactGroup;
import com.ensah.smartcontact.models.User;

public interface ContactGroupService {
	
	ContactGroup getById(Long id);
	
	void save(ContactGroup cgroup);
	
	List<ContactGroup> getAllContactGroup();
	
	List<ContactGroup> getByUser(User user);
	
	ContactGroup findByIdAndUser(Long id, User user);

	void delete(ContactGroup g);
	
}
