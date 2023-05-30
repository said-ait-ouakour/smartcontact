package com.ensah.smartcontact.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ensah.smartcontact.dao.ContactRepository;
import com.ensah.smartcontact.dao.UserRepository;
import com.ensah.smartcontact.models.Contact;
import com.ensah.smartcontact.models.User;
import com.ensah.smartcontact.services.ContactService;
import com.ensah.smartcontact.services.UserService;
import com.ensah.smartcontact.web.dto.ContactDto;

@Controller
public class ContactController {

	Logger logger = LoggerFactory.getLogger(ContactController.class);

	@Autowired
	ContactService contactService;

	@Autowired
	UserService userService;

	@Autowired
	private ResourceLoader resourceLoader;

	public ContactController(ContactService contactService, ResourceLoader resourceLoader,
			ContactRepository contactRepository, UserService userService, UserRepository userRepository) {
		super();
		this.resourceLoader = resourceLoader;
		this.contactService = contactService;
		this.userService = userService;
	}

	@GetMapping("/contact/add")
	public String showContactForm(Model model) {
		
		model.addAttribute("context", "Create Contact");

		
		return "contact/addContact";
	}

	@ModelAttribute("contact")
	public ContactDto contactDao() {
		return new ContactDto();
	}

	@PostMapping("/contact/add")
	public String addContact(@RequestParam("profileImage") MultipartFile file,
			@ModelAttribute("contact") ContactDto contactDao, Principal principal) throws Exception {

		String username = principal.getName();

		User user = this.userService.findUserByUsernameOrEmail(username, username);

		Contact contact = new Contact(contactDao.getFirstname(), contactDao.getLastname(),
				contactDao.getPersonalEmail(), contactDao.getProEmail(), contactDao.getPhone1(), contactDao.getPhone2(),
				contactDao.getAddress(), contactDao.getGender(), user);

		if (!file.isEmpty()) {
			contact.setImage(file.getOriginalFilename());

			File saveFile = resourceLoader.getResource("classpath:static/img/").getFile();

			Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

		}

		user.addContact(contact);

		this.userService.save(user);
		
		this.contactService.save(contact, user);

		return "redirect:/contact/add?success";
	}

	@GetMapping("/contact/{id}")
	public String editContact(@PathVariable Long id, Model model, Principal principal) {

		try {
			String username = principal.getName();

			User user = this.userService.findUserByUsernameOrEmail(username, username);

			Contact contact = this.contactService.findByIdAndUser(id, user);

			if (contact != null) {
				model.addAttribute("contact", contact);
				model.addAttribute("context", "Update Contact");

				return "contact/updateContact";
			}

			return "error";

		} catch (Exception e) {
			logger.error(e.getMessage());
			return "redirect:/contact/add?error";
		}

	}

	@GetMapping("/contact/list")
	public String getListOfContacts(Model model, Principal principal) {

		String username = principal.getName();

		User user = this.userService.findUserByUsernameOrEmail(username, username);

		List<Contact> contacts = user.getContacts();

		if (contacts == null) {
			return "redirect:/contact/add";
		}

		model.addAttribute("contacts", contacts);
		return "contact/listContacts";
	}

	@RequestMapping("/contact/remove/{id}")
	public String removeContact(@PathVariable Long id, Principal principal) {
		String username = principal.getName();

		User user = this.userService.findUserByUsernameOrEmail(username, username);

		for (Contact c : user.getContacts()) {
			if (c.getId() == id) {
				this.contactService.deleteById(id);
				return "redirect:/contact/list?remove=true";
			}
		}
		return "redirect:/contact/list?remove=false";
	}

	@PostMapping("/contact/{id}")
	public String updateContact(@RequestParam("profileImage") MultipartFile file, @PathVariable Long id,
			@ModelAttribute("contact") ContactDto contactDao, Principal principal) throws IOException {

		String username = principal.getName();

		User user = this.userService.findUserByUsernameOrEmail(username, username);

		Contact contact = new Contact(id, contactDao.getFirstname(), contactDao.getLastname(),
				contactDao.getPersonalEmail(), contactDao.getPersonalEmail(), contactDao.getPhone1(),
				contactDao.getPhone2(), contactDao.getAddress(), contactDao.getGender(), user);

		if (file.isEmpty()) {
			contact.setImage(this.contactService.getById(id).getImage());
		} else {
			contact.setImage(file.getOriginalFilename());

			File saveFile = resourceLoader.getResource("classpath:static/img/").getFile();

			Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

		}

		contactService.save(contact, user);

		return "redirect:/contact/list?updateSuccess";

	}
}
