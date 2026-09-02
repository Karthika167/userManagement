package com.avio.view;

import java.util.ArrayList;

public class RoleListResponse {

	String status;

	ArrayList<RoleView> role;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<RoleView> getRole() {
		return role;
	}

	public void setRole(ArrayList<RoleView> role) {
		this.role = role;
	}
}
