package com.ensah.smartcontact.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

import com.ensah.smartcontact.models.Contact;
import com.ensah.smartcontact.models.ContactGroup;
import com.ensah.smartcontact.models.User;
import com.ensah.smartcontact.services.ContactGroupService;
import com.ensah.smartcontact.services.ContactService;
import com.ensah.smartcontact.services.UserService;
import com.ensah.smartcontact.web.dto.GroupDto;

@Controller
public class GroupController {

	@Autowired
	UserService userService;

	@Autowired
	ContactService contactService;

	@Autowired
	ContactGroupService contactGroupService;

	@Autowired
	private ResourceLoader resourceLoader;

	public GroupController(UserService userService, ContactService contactService,
			ContactGroupService contactGroupService, ResourceLoader resourceLoader) {
		super();
		this.userService = userService;
		this.contactService = contactService;
		this.contactGroupService = contactGroupService;
		this.resourceLoader = resourceLoader;
	}

	@ModelAttribute("group")
	public GroupDto groupDto() {
		return new GroupDto();
	}

	@GetMapping("group/list")
	public String listGroups(Model model, Principal p) {

		String username = p.getName();

		User user = this.userService.findUserByUsernameOrEmail(username, username);

		List<ContactGroup> groups = this.contactGroupService.getByUser(user);

		if (groups == null) {
			return "redirect:/group/add";
		}

		model.addAttribute("groups", groups);

		return "group/listGroups";
	}

	@GetMapping("group/add")
	public String addGroup(Model model) {
		model.addAttribute("context", "Create Group");

		return "group/addGroup";
	}

	@PostMapping("group/add")
	public String addGroup(@RequestParam("profileImage") MultipartFile file, @ModelAttribute("group") GroupDto groupDto,
			Principal principal) {

		String username = principal.getName();

		User user = userService.findUserByUsernameOrEmail(username, username);

		try {

			List<Contact> contacts = new ArrayList<Contact>();
			String groupName = groupDto.getName();
			String groupDescription = groupDto.getDescription();
			for (Long cId : groupDto.getContactIds()) {
				Contact c = contactService.getById(cId);
				if (c != null) {
					contacts.add(c);
				}
			}

			Set<Contact> contactsSet = new HashSet<Contact>(contacts);

			ContactGroup contactGroup = new ContactGroup(groupName, user, contactsSet, groupDescription);

			if (!file.isEmpty()) {

				contactGroup.setImage(file.getOriginalFilename());
				File saveFile = resourceLoader.getResource("classpath:static/img/").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}
			
			user.getGroups().add(contactGroup);
			
			this.userService.save(user);

			this.contactGroupService.save(contactGroup);

		} catch (Exception e) {

			return "redirect:/group/list?error";
		}

		return "redirect:/group/list?success";

	}

	@GetMapping("/group/{id}")
	public String updateGroup(@PathVariable Long id, Model model, Principal principal) {

		try {
			String username = principal.getName();

			User user = this.userService.findUserByUsernameOrEmail(username, username);

			ContactGroup contactGroup = this.contactGroupService.findByIdAndUser(id, user);

			if (contactGroup != null) {
				GroupDto groupDto = new GroupDto();

				groupDto.setDescription(contactGroup.getDescription());

				List<Long> contactIds = contactGroup.getContacts().stream().map(Contact::getId)
						.collect(Collectors.toList());

				groupDto.setContactIds(contactIds);

				groupDto.setName(contactGroup.getName());

				Map<Long, String> contactMap = new HashMap<>();

				for (Contact c : contactGroup.getContacts()) {
					contactMap.put(c.getId(), c.getFirstname() + " " + c.getLastname());
				}

				
				model.addAttribute("context", "Update Group");
				model.addAttribute("groupId", contactGroup.getId());
				model.addAttribute("group", groupDto);
				model.addAttribute("contactmap", contactMap);
				model.addAttribute("image", contactGroup.getImage());

				return "group/updateGroup";
			}

			return "group/updateGroup?error";

		} catch (Exception e) {
			return "group/updateGroup?error";
		}
	}

	@PostMapping("/group/{id}")
	public String updateGroupPost(@RequestParam("groupImage") MultipartFile file, @PathVariable Long id,
			@ModelAttribute("group") GroupDto groupDao, Principal principal) throws IOException {

		String username = principal.getName();

		User user = this.userService.findUserByUsernameOrEmail(username, username);

		List<Contact> contacts = groupDao.getContactIds().stream().map(i -> contactService.getById(i))
				.collect(Collectors.toList());

		ContactGroup cGroup = new ContactGroup(groupDao.getName(), user, new HashSet<Contact>(contacts),
				groupDao.getDescription());

		cGroup.setId(id);

		if (file.isEmpty()) {
			cGroup.setImage(this.contactGroupService.getById(id).getImage());
		} else {
			cGroup.setImage(file.getOriginalFilename());

			File saveFile = resourceLoader.getResource("classpath:static/img/").getFile();

			Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

		}

		contactGroupService.save(cGroup);

		return "redirect:/group/list?updateSuccess";

	}

	@RequestMapping("/group/remove/{id}")
	public String removeContact(@PathVariable Long id, Principal principal) {

		String username = principal.getName();

		User user = this.userService.findUserByUsernameOrEmail(username, username);

		List<ContactGroup> groups = this.contactGroupService.getByUser(user);

		for (ContactGroup g : groups) {
			if (g.getId() == id) {
				this.contactGroupService.delete(g);
				return "redirect:/group/list?remove=true";
			}
		}

		return "redirect:/group/list?remove=false";
	}

}
