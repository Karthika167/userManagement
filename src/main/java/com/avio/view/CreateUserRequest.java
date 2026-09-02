package com.avio.view;

import java.util.List;
import java.util.UUID;

import com.avio.dao.model.Organization;

public class CreateUserRequest {

	private String firstName;

	private String lastName;

	private String email;

	private String password;

	private Organization organization;

	private String phoneNumber;
	
	private List<UUID> roles; 

	private boolean active = true;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<UUID> getRoles() {
		return roles;
	}

	public void setRoles(List<UUID> roles) {
		this.roles = roles;
	}

}