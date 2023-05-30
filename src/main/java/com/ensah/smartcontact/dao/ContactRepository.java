package com.ensah.smartcontact.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ensah.smartcontact.models.Contact;
import com.ensah.smartcontact.models.User;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

	public List<Contact> findAllByUser(User user);

	// Find by full name containing

	@Query("SELECT c FROM Contact c WHERE CONCAT(c.firstname, ' ', c.lastname) LIKE %:fullName% AND c.user = :user")
	public List<Contact> findByFullNameContainingAndUser(String fullName, User user);

	// Find by first name containing
	public List<Contact> findByFirstnameContainingAndUser(String firstname, User user);

	// Find by last name containing
	public List<Contact> findByLastnameContainingAndUser(String firstname, User user);

	public Contact findByIdAndUser(Long id, User user);
	
	
//	find by Phone Numbers professional
	public List<Contact> findByPhone1ContainingAndUser(String phone, User user);

	//	find by Phone Numbers Home number
	public List<Contact> findByPhone2ContainingAndUser(String phone, User user);
	
	
//	find by gender
	public List<Contact> findByGenderAndUser(String gender, User user);

	
	@Modifying
	@Query("DELETE FROM Contact c WHERE c.id = :id")
	public void deleteById(Long id);

}
