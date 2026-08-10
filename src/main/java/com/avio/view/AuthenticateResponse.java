package com.avio.view;

import java.util.UUID;

public class AuthenticateResponse {
	

	private String status;
	
	private UserDetailsView userDetails;

	private UUID session;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public UserDetailsView getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetailsView userDetails) {
		this.userDetails = userDetails;
	}

	public UUID getSession() {
		return session;
	}

	public void setSession(UUID session) {
		this.session = session;
	}
		
	
	
}
