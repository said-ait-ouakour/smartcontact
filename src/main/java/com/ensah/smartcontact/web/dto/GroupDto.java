package com.ensah.smartcontact.web.dto;

import java.util.List;

public class GroupDto {

	/**
	 * 
	 * @author Said AIT OUAKOUR
	 * 
	 */

	private String name;
	private List<Long> contactIds;
	private String description;

	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Long> getContactIds() {
		return contactIds;
	}

	public void setContactIds(List<Long> contactIds) {
		this.contactIds = contactIds;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	
}
