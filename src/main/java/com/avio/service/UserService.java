package com.avio.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.avio.config.Base64Example;
import com.avio.dao.PersonnelDao;
import com.avio.dao.SessionDao;
import com.avio.dao.UserDao;
import com.avio.dao.UserRoleDao;
import com.avio.dao.model.User;
import com.avio.dao.model.UserRole;
import com.avio.dao.repository.UserRepository;

import com.avio.validator.RequestValidator;
import com.avio.view.AuthenticateRequest;
import com.avio.view.AuthenticateResponse;
import com.avio.view.PasswordRequest;
import com.avio.view.PasswordResponse;
import com.avio.view.UserDetailsView;
import com.avio.view.UserUpadateRequest;

import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;

import com.avio.dao.model.Session;

@Service
public class UserService {

	@Autowired
	UserDao userDao;

	
	@Autowired
	
	SessionDao sessionDao;

	@Autowired
	UserRoleDao userRoleDao;
	
	@Autowired
	PersonnelDao personnelDao;

	@SuppressWarnings("unused")
	public ResponseEntity<AuthenticateResponse> authenticateUser(AuthenticateRequest request, String ipAddress,
			String userAgent) throws Exception {

		RequestValidator.validateAuthenticaterequest(request);

		User user = userDao.authenticateUser(request.getUsername(), request.getPassword(), request.getEmail());

		List<UserRole> userRoles = userRoleDao.getUserRole(user.getUserId());
		ArrayList<String> roles = new ArrayList<>();
		for (UserRole userRole : userRoles) {
			roles.add(userRole.getRole().getRoleName());
		}

		String tokenHash = "this is token";
		Session session = (Session) sessionDao.createSession(user, tokenHash, ipAddress, userAgent);
		 

		
		AuthenticateResponse response = new AuthenticateResponse();
		response.setStatus("Sucess");

		UserDetailsView userDetails = new UserDetailsView();

		userDetails.setFirstName(user.getPersonnel().getFirstName());
		userDetails.setLastName(user.getPersonnel().getLastName());
		userDetails.setEmail(user.getPersonnel().getEmail());
		userDetails.setPhoneNumber(user.getPersonnel().getPhoneNumber());
		userDetails.setDepartment(user.getPersonnel().getRole().getDisplayName());
		userDetails.setUserRoles(roles);
		userDetails.setCreatedAt(String.valueOf(user.getCreatedAt()));

		response.setUserDetails(userDetails);
		response.setSession(session.getSessionId());
		return ResponseEntity.ok().body(response);

	}
//Password change
	public ResponseEntity<PasswordResponse> passwordChange(PasswordRequest passwordRequest) throws Exception {

		RequestValidator.ValidatePassword(passwordRequest);

		userDao.changePassword(passwordRequest.getEmail(), passwordRequest.getUsername(),
				passwordRequest.getCurrentPassword(), passwordRequest.getNewPassword());

		PasswordResponse passwordResponse = new PasswordResponse();
		passwordResponse.setStatus("SUCCESS");
		passwordResponse.setMessgae("Pasword updated successfully");
		return ResponseEntity.ok().body(passwordResponse);
	}

//	profile update
	public ResponseEntity<PasswordResponse> updateUser(UserUpadateRequest userUpadateRequest) throws Exception {

		RequestValidator.ValidateUserUpdate(userUpadateRequest);

		personnelDao.updateUser(userUpadateRequest);

		PasswordResponse passwordResponse = new PasswordResponse();
		passwordResponse.setStatus("SUCCESS");
		passwordResponse.setMessgae("Pasword updated successfully");
		return ResponseEntity.ok().body(passwordResponse);
	}
	public void logout(UUID sessionId) {
		sessionDao.logout(sessionId);
	}

}
