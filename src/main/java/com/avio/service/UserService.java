package com.avio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.avio.config.Base64Example;
import com.avio.dao.UserDao;
import com.avio.dao.UserRoleDao;
import com.avio.dao.model.User;
import com.avio.dao.model.UserRole;
import com.avio.validator.RequestValidator;
import com.avio.view.AuthenticateRequest;
import com.avio.view.AuthenticateResponse;

import jakarta.transaction.Transactional;

@Service
public class UserService {

	@Autowired
	UserDao userDao;

	@Autowired
	UserRoleDao userRoleDao;

	public ResponseEntity<AuthenticateResponse> authenticateUser(AuthenticateRequest request, String ipAddress, String userAgent) throws Exception {

		RequestValidator.validateAuthenticaterequest(request);

		User user = userDao.authenticateUser(request.getUsername(), request.getPassword(), request.getEmail());

		List<UserRole> userRoles = userRoleDao.getUserRole(user.getUserId());
		
		UserRole userRole = userRoles.get(0);
		

		AuthenticateResponse response = new AuthenticateResponse();
		response.setStatus("Sucess");
		response.setUserRole(userRole.getRole().getRoleName());
		response.setOrganization(userRole.getOrganization().getName());
		response.setFirstName(user.getPersonnel().getFirstName());
		response.setLastName(user.getPersonnel().getLastName());

		return ResponseEntity.ok().body(response);
	}

}
