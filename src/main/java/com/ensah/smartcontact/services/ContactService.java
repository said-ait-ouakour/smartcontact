package com.ensah.smartcontact.services;

import java.util.List;
import java.util.Optional;


import com.ensah.smartcontact.exceptions.ContactNotFoundException;
import com.ensah.smartcontact.models.Contact;
import com.ensah.smartcontact.models.User;

public interface ContactService {

//	save contact 
	Contact save(Contact contact, User user);

//  find all
	List<Contact> findAll();

//	find by id 
	Optional<Contact> findById(Long id) throws ContactNotFoundException;
	
//	male contacts
	List<Contact> maleContacts(User user);
	
	List<Contact> femaleContacts(User user);
		
	List<Contact> searchForContact(String query, User user);

	Contact findByIdAndUser(Long id, User user);

	void deleteById(Long id);

	Contact getById(Long id);

}
