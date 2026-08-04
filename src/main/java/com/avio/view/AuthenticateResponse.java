package com.avio.view;

public class AuthenticateResponse {
	

	private String status;
	
	private UserDetailsView userDetails;

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
		
	
	
}
