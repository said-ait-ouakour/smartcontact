package com.ensah.smartcontact.web;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ensah.smartcontact.dao.UserRepository;
import com.ensah.smartcontact.models.Contact;
import com.ensah.smartcontact.models.User;
import com.ensah.smartcontact.services.ContactGroupService;
import com.ensah.smartcontact.services.ContactService;
import com.ensah.smartcontact.web.dto.UserDto;

@Controller
public class MainController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ContactService contactService;

	@Autowired
	ResourceLoader resourceLoader;
	
	@Autowired
	ContactGroupService contactGroupService;

	public MainController(ContactGroupService contactGroupService, ContactService contactService,
			UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
		this.contactService = contactService;
		this.contactGroupService = contactGroupService;
	}
	
	
	@GetMapping("/login")
	public String showLoginForm(Principal principal) {

		if (principal != null) {
			return "redirect:/";
		}

		return "user/login";
	}

	@GetMapping("/")
	public String index(Model model, Principal principal) {

		String username = principal.getName();

		User user = userRepository.findByUsername(username);

		model.addAttribute("name", StringUtils.capitalize(user.getFirstname()));

		model.addAttribute("contact_number", user.getContacts().size());

		// Create a new Date object
		Date date = new Date();

		// Create a SimpleDateFormat object
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMM yyyy");

		String formattedDate = sdf.format(date);

		model.addAttribute("today", formattedDate);

		List<Contact> maleContacts = contactService.maleContacts(user);
		List<Contact> femaleContacts = contactService.femaleContacts(user);

		model.addAttribute("group_number", this.contactGroupService.getByUser(user).size());

		model.addAttribute("male_contacts", maleContacts.size());
		model.addAttribute("female_contacts", femaleContacts.size());

		return "index";
	}

	@GetMapping("/user/settings")
	public String getSettings(Model model, Principal p) {
		
		try {
			
			String username = p.getName();
			
			System.out.println(username);
			
			User user = userRepository.findByUsernameOrEmail(username, username);
			
			model.addAttribute("user", user);
			
			return "user/settings";
			
		} catch (Exception e) {
			return "error";
		}
		
	}
	
	@PostMapping("/user/settings")
	public String updateSettings(@RequestParam("profileImage") MultipartFile file,
			@ModelAttribute("user") UserDto userDto,Principal p) {
		
		try {

			String username = p.getName();
			
			User user = userRepository.findByUsernameOrEmail(username, username);
			
			user.setUsername(userDto.getUsername());
			
			user.setFirstname(userDto.getFirstname());
			
			user.setLastname(userDto.getLastname());
			
			user.setEmail(userDto.getEmail());
			
			if (!file.isEmpty()) {
				user.setImage(file.getOriginalFilename());

				File saveFile = resourceLoader.getResource("classpath:static/img/").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			}

			this.userRepository.save(user);


			@SuppressWarnings("unchecked")
			Collection<SimpleGrantedAuthority> nowAuthorities = (Collection<SimpleGrantedAuthority>)SecurityContextHolder.getContext().getAuthentication().getAuthorities();
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDto.getUsername(), user.getPassword(), nowAuthorities);

			SecurityContextHolder.getContext().setAuthentication(authentication);		
			
	        return "redirect:/user/settings?saved";
			
		} catch (Exception e) {
			return "error";
		}
		
	}

}
