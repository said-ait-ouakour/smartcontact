package com.ensah.smartcontact.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String firstname;
	private String lastname;
	private String personalEmail;
	private String proEmail;
	private String phone1;
	private String phone2;
	private String address;
	private String gender;

	@CreatedDate
	@Column(name = "created_at")
	private Date createdAt;

	private String image;

	@ManyToOne
	@JsonIgnore
	private User user;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ContactGroup> groups;


	public Contact() {
	}

	public Contact(Long id, String firstname, String lastname, String personalEmail, String proEmail, String phone1,
			String phone2, String address, String gender, User user) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.personalEmail = personalEmail;
		this.proEmail = proEmail;
		this.phone1 = phone1;
		this.phone2 = phone2;
		this.address = address;
		this.gender = gender;
		this.user = user;
	}

	public Contact(String firstname, String lastname, String personalEmail, String proEmail, String phone1,
			String phone2, String address, String gender, User user) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.personalEmail = personalEmail;
		this.proEmail = proEmail;
		this.phone1 = phone1;
		this.phone2 = phone2;
		this.address = address;
		this.gender = gender;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}
