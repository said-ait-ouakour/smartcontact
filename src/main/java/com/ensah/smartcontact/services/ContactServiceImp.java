package com.ensah.smartcontact.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ensah.smartcontact.dao.ContactRepository;
import com.ensah.smartcontact.exceptions.ContactNotFoundException;
import com.ensah.smartcontact.models.Contact;
import com.ensah.smartcontact.models.User;

@Service
public class ContactServiceImp implements ContactService {

	@Autowired
	ContactRepository contactRepository;

	@Autowired
	ContactGroupService contactGroupService;

	@Override
	public Contact save(Contact contact, User user) {
		
//		check if there is already a contact with the same name 
		
		List<Contact> contacts = contactRepository.findAllByUser(user);
		
		for (Contact c: contacts) {
			if (c.getLastname()==contact.getLastname()) {
				
			}
		}
		
		return contactRepository.save(contact);
	}

	@Override
	public List<Contact> findAll() {
		return contactRepository.findAll();
	}

	@Override
	public Optional<Contact> findById(Long id) throws ContactNotFoundException {
		Optional<Contact> contact = contactRepository.findById(id);
		if (contact == null) {
			throw new ContactNotFoundException("Contact not found");
		}
		return contact;

	}

	@Override
	public List<Contact> maleContacts(User user) {
		return contactRepository.findByGenderAndUser("male", user);
	}

	@Override
	public List<Contact> femaleContacts(User user) {
		return contactRepository.findByGenderAndUser("female", user);
	}

	public List<Contact> searchForContact(String query, User user) {

		List<Contact> allContacts = this.contactRepository.findAllByUser(user);

		Set<Contact> contactsFn = new HashSet<>();
		Set<Contact> contactsLn = new HashSet<>(this.contactRepository.findByLastnameContainingAndUser(query, user));

		Set<Contact> contactsF = new HashSet<>(this.contactRepository.findByFullNameContainingAndUser(query, user));

		contactsFn.addAll(contactsLn);
		contactsF.addAll(contactsFn);

		int thresholdForName = 10;
		int thresholdForPhoneNumber = 7;

		LevenshteinDistance dist = new LevenshteinDistance();

		for (Contact c : allContacts) {

			String fullname = c.getFirstname().toLowerCase() + c.getLastname().toLowerCase();
			String number1 = c.getPhone1();
			String number2 = c.getPhone2();

			if (dist.apply(query.toLowerCase(), fullname) < thresholdForName) {
				contactsF.add(c);
			}

			if (dist.apply(query.toLowerCase(), number2) < thresholdForPhoneNumber) {
				contactsF.add(c);
			}

			if (dist.apply(query.toLowerCase(), number1) < thresholdForPhoneNumber) {
				contactsF.add(c);
			}

		}

		return new ArrayList<>(contactsF);

	}

	@Override
	public Contact findByIdAndUser(Long id, User user) {
		Contact contact = this.contactRepository.findByIdAndUser(id, user);
		return contact;
	}

	@Override
	public void deleteById(Long id) {
		this.contactRepository.deleteById(id);
	}

	@SuppressWarnings("deprecation")
	@Override
	public Contact getById(Long id) {
		
		return this.contactRepository.getById(id);
	}

}
