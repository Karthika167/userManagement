package com.avio.view;

public class CreateUserResponse {

	private String status;

	private String messgae;
	
	private UserDetailsView user;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessgae() {
		return messgae;
	}

	public void setMessgae(String messgae) {
		this.messgae = messgae;
	}

	public UserDetailsView getUser() {
		return user;
	}

	public void setUser(UserDetailsView user) {
		this.user = user;
	}

}
