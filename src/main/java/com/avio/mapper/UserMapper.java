package com.avio.mapper;

import com.avio.dao.model.User;
import com.avio.view.CreateUserResponse;
import com.avio.view.UserDetailsView;

public class UserMapper {

	public static CreateUserResponse mapCreateUserResponse(User userResp) {

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

		createUserResponse.setUser(user);
		
		return createUserResponse;
	}
}
