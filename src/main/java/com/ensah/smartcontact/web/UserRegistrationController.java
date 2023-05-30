package com.ensah.smartcontact.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ensah.smartcontact.models.User;
import com.ensah.smartcontact.services.UserService;
import com.ensah.smartcontact.web.dto.SignUpDto;

@Controller
@RequestMapping("/signup")
public class UserRegistrationController {

	private UserService userService;

	public UserRegistrationController(UserService userService) {
		super();
		this.userService = userService;
	}

	@ModelAttribute("user")
	public SignUpDto signUpDto() {
		return new SignUpDto();
	}

	@GetMapping
	public String showSignUpFrom() {
		return "user/signup";
	}

	@PostMapping
	public String registerUserAccount(@ModelAttribute("user") SignUpDto signUpDto) {

		String email = signUpDto.getEmail();
		String username = signUpDto.getUsername();

		User user = userService.findUserByUsernameOrEmail(username, email);

//		does not implement the concurrent problem 
		if (user == null) { 
			userService.save(signUpDto);
			return "redirect:/login";

		}
		else {
			return "redirect:/signup?userExist";
		}
	}
}
