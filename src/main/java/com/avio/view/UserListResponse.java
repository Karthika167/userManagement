package com.avio.view;

import java.util.ArrayList;

public class UserListResponse {
	
	String status;
	
	ArrayList<UserDetailsView> users;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<UserDetailsView> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<UserDetailsView> users) {
		this.users = users;
	}

}
