package com.ensah.smartcontact.web;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ensah.smartcontact.models.Contact;
import com.ensah.smartcontact.models.User;
import com.ensah.smartcontact.services.ContactService;
import com.ensah.smartcontact.services.UserService;

@Controller
public class MessageController {
	
	Logger logger = LoggerFactory.getLogger(MessageController.class);


	@Autowired
	UserService userService;

	@Autowired
	ContactService contactService;

	@GetMapping("/message")
	public String getMessages() {
		return "message/listMessage";
	}

	@GetMapping("/message/{id}")
	public String getContactMessages(Model model, @PathVariable Long id, Principal principal) {

		String username = principal.getName();

		User user = this.userService.findUserByUsernameOrEmail(username, username);

		Contact contact = this.contactService.findByIdAndUser(id, user);

		model.addAttribute("contact", contact);

		return "message/sendMessage";
	}

}
