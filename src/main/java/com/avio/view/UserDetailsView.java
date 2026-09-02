package com.avio.view;

import java.util.ArrayList;
import java.util.UUID;

public class UserDetailsView {

	private UUID userId;
	
	private String firstName;

	private String lastName;

	private String email;

	private String phoneNumber;

	private String department;

	private String createdAt;

	private boolean isActive;

	private ArrayList<RoleView> userRoles;

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public ArrayList<RoleView> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(ArrayList<RoleView> userRoles) {
		this.userRoles = userRoles;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
