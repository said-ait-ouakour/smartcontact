package com.ensah.smartcontact.web;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ensah.smartcontact.models.Contact;
import com.ensah.smartcontact.models.User;
import com.ensah.smartcontact.services.ContactService;
import com.ensah.smartcontact.services.UserService;

@RestController
public class SearchController {

	@Autowired
	UserService userService;

	@Autowired
	ContactService contactService;

	public SearchController(ContactService contactService, UserService userService) {
		this.contactService = contactService;
		this.userService = userService;
	}

	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable(name = "query") String query, Principal principal) {
		String username = principal.getName();

		User user = this.userService.findUserByUsernameOrEmail(username, username);

		List<Contact> contacts = this.contactService.searchForContact(query, user);

		return ResponseEntity.ok(contacts);
	}

}
