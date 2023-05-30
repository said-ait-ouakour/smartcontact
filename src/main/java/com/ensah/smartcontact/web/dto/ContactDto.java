package com.ensah.smartcontact.web.dto;

public class ContactDto {

	private String firstname;
	private String lastname;
	private String personalEmail;
	private String proEmail;
	private String phone1;
	private String phone2;
	private String address;
	private String gender;
	
	

	public ContactDto() {
	}

	public ContactDto(String firstname, String lastname, String personalEmail, String proEmail, String phone1,
			String phone2, String address, String gender) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.personalEmail = personalEmail;
		this.proEmail = proEmail;
		this.phone1 = phone1;
		this.phone2 = phone2;
		this.address = address;
		this.gender = gender;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPersonalEmail() {
		return personalEmail;
	}

	public void setPersonalEmail(String personalEmail) {
		this.personalEmail = personalEmail;
	}

	public String getProEmail() {
		return proEmail;
	}

	public void setProEmail(String proEmail) {
		this.proEmail = proEmail;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

}
