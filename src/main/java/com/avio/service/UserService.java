package com.avio.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.avio.config.Base64Example;
import com.avio.dao.SessionDao;
import com.avio.dao.UserDao;
import com.avio.dao.UserRoleDao;
import com.avio.dao.model.User;
import com.avio.dao.model.UserRole;
import com.avio.validator.RequestValidator;
import com.avio.view.AuthenticateRequest;
import com.avio.view.AuthenticateResponse;
import com.avio.view.UserDetailsView;
import com.avio.dao.model.Session;

@Service
public class UserService {

 @Autowired
   UserDao userDao;
	
	@Autowired
	SessionDao sessionDao;

	@Autowired
	UserRoleDao userRoleDao;

	@SuppressWarnings("unused")
	public ResponseEntity<AuthenticateResponse> authenticateUser(AuthenticateRequest request, String ipAddress, String userAgent) throws Exception {

		RequestValidator.validateAuthenticaterequest(request);

		User user = userDao.authenticateUser(request.getUsername(), request.getPassword(), request.getEmail());

		List<UserRole> userRoles = userRoleDao.getUserRole(user.getUserId());
		ArrayList<String> roles = new ArrayList<>();
		for (UserRole userRole : userRoles) {
			roles.add(userRole.getRole().getRoleName());
		}
		String tokenHash="this is token";
		Session session= (Session) sessionDao.createSession(user,tokenHash,ipAddress,userAgent);

		AuthenticateResponse response = new AuthenticateResponse();
		response.setStatus("Sucess");
		UserDetailsView userDetails = new UserDetailsView();
				
		userDetails.setFirstName(user.getPersonnel().getFirstName());
		userDetails.setLastName(user.getPersonnel().getLastName());
		userDetails.setEmail(user.getPersonnel().getEmail());
		userDetails.setPhoneNumber("XXXXXX");
		userDetails.setDepartment(user.getPersonnel().getRole().getDisplayName());
		userDetails.setUserRoles(roles);
		
		response.setUserDetails(userDetails );
		response.setUserRole(roles);
		response.setOrganization(user.getPersonnel().getRole().getDisplayName());
		response.setFirstName(user.getPersonnel().getFirstName());
		response.setLastName(user.getPersonnel().getLastName());

		return ResponseEntity.ok().body(response);
	}

}
