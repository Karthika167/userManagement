package com.avio.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.avio.dao.model.Organization;
import com.avio.dao.model.Personnel;
import com.avio.dao.model.PersonnelRole;
import com.avio.dao.model.User;
import com.avio.dao.model.UserRole;
import com.avio.view.CreateUserRequest;
import com.avio.view.CreateUserResponse;
import com.avio.view.RoleView;
import com.avio.view.UserDetailsView;
import com.avio.view.UserListResponse;

public class UserMapper {

	public static CreateUserResponse mapCreateUserResponse(User userResp, List<UserRole> userRoles) {

		CreateUserResponse createUserResponse = new CreateUserResponse();

		createUserResponse.setStatus("SUCCESS");
		createUserResponse.setMessgae("User created successfully");

		UserDetailsView user = new UserDetailsView();

		user.setUserId(userResp.getUserId());
		user.setFirstName(userResp.getPersonnel().getFirstName());
		user.setLastName(userResp.getPersonnel().getLastName());
		user.setActive(userResp.isActive());
		user.setCreatedAt(String.valueOf(userResp.getCreatedAt()));
		user.setEmail(userResp.getPersonnel().getEmail());
		user.setPhoneNumber(userResp.getPersonnel().getPhoneNumber());
		user.setUserRoles(mapUserRole(userRoles));
		createUserResponse.setUser(user);

		return createUserResponse;
	}

	public static ArrayList<RoleView> mapUserRole(List<UserRole> userRoles) {
		ArrayList<RoleView> roles = new ArrayList<RoleView>();
		RoleView roleView;
		for (UserRole userRole : userRoles) {
			roleView = new RoleView();
			roleView.setRoleId(userRole.getRole().getRoleId());     // Role's actual ID       
//			roleView.setRoleId(userRole.getUrId());
			roleView.setRoleName(userRole.getRole().getRoleName());  // e.g. "QA Auditor"
			roles.add(roleView);
		}
		return roles;
	}

	// Add New user By Admin
	public static User mapCreateUser(CreateUserRequest userRequest, Organization org) {

// set in personnel
		Personnel personnel = new Personnel();
		personnel.setFirstName(userRequest.getFirstName());
		personnel.setLastName(userRequest.getLastName());
		personnel.setEmail(userRequest.getEmail());
		personnel.setPhoneNumber(userRequest.getPhoneNumber());
		personnel.setOrganization(org);
		personnel.setRole(PersonnelRole.QA_AUDITOR); // single

		personnel.setActive(userRequest.isActive());
//set in user
		User user = new User();
		user.setUsername(userRequest.getFirstName()); // make sure this exists on the DTO
		user.setEmail(userRequest.getEmail());
		user.setPasswordHash(userRequest.getPassword());
		user.setOrganization(org);

		user.setPersonnel(personnel); // set personnelId in user
		user.setActive(userRequest.isActive());
		user.setCreatedAt(LocalDateTime.now()); // required, not-null column
		return user;
	}

}
