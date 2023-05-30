package com.ensah.smartcontact.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ensah.smartcontact.dao.ContactGroupRepository;
import com.ensah.smartcontact.models.ContactGroup;
import com.ensah.smartcontact.models.User;


@Service
public class ContactGroupServiceImp implements ContactGroupService {

	@Autowired
	ContactGroupRepository contactGroupRepository;
	
	@SuppressWarnings("deprecation")
	@Override
	public ContactGroup getById(Long id) {
		return contactGroupRepository.getById(id);
	}

	@Override
	public void save(ContactGroup cgroup) {
		contactGroupRepository.save(cgroup);
	}

	@Override
	public List<ContactGroup> getAllContactGroup() {
		return contactGroupRepository.findAll();
	}

	@Override
	public List<ContactGroup> getByUser(User user) {
		return this.contactGroupRepository.findByUser(user);
	}

	@Override
	public ContactGroup findByIdAndUser(Long id, User user) {
		return this.contactGroupRepository.findByIdAndUser(id, user);
	}

	@Override
	public void delete(ContactGroup g) {
		this.contactGroupRepository.delete(g);
	}

}
