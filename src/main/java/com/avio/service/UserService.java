package com.avio.service;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.avio.config.Base64Example;
import com.avio.dao.OrganizationDao;
import com.avio.dao.PersonnelDao;
import com.avio.dao.SessionDao;
import com.avio.dao.UserDao;
import com.avio.dao.UserRoleDao;
import com.avio.dao.model.User;
import com.avio.dao.model.UserRole;
import com.avio.dao.repository.UserRepository;
import com.avio.mapper.UserMapper;
import com.avio.util.JwtUtil;
import com.avio.validator.RequestValidator;
import com.avio.view.AuthenticateRequest;
import com.avio.view.AuthenticateResponse;
import com.avio.view.CreateUserRequest;
import com.avio.view.CreateUserResponse;
import com.avio.view.PasswordRequest;
import com.avio.view.PasswordResponse;
import com.avio.view.UserDetailsView;
import com.avio.view.UserListResponse;
import com.avio.view.UserProfileUpadateRequest;
import com.avio.view.UserUpadateRequest;

import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;

import com.avio.dao.model.Organization;
import com.avio.dao.model.Personnel;
import com.avio.dao.model.PersonnelRole;
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

	@Autowired
	OrganizationDao organizationDao;

	@Autowired
	JwtUtil jwtUtil;

	// User Login

	public ResponseEntity<AuthenticateResponse> authenticateUser(AuthenticateRequest request, String ipAddress,
			String userAgent) throws Exception {

		RequestValidator.validateAuthenticaterequest(request);

		User user = userDao.authenticateUser(request.getUsername(), request.getPassword(), request.getEmail());

		List<UserRole> userRoles = userRoleDao.getUserRole(user.getUserId());
		ArrayList<String> roles = new ArrayList<>();
		for (UserRole userRole : userRoles) {
			roles.add(userRole.getRole().getRoleName());
		}

		// String tokenHash = "this is token";
		String tokenHash = jwtUtil.generateToken(user.getUserId(), user.getEmail(), List.of()); // roles empty list for
																								// now, wire in later
		Session session = (Session) sessionDao.createSession(user, tokenHash, ipAddress, userAgent);

		AuthenticateResponse response = new AuthenticateResponse();
		response.setStatus("Sucess");

		UserDetailsView userDetails = new UserDetailsView();
		userDetails.setUserId(user.getUserId());
		userDetails.setFirstName(user.getPersonnel().getFirstName());
		userDetails.setLastName(user.getPersonnel().getLastName());
		userDetails.setEmail(user.getPersonnel().getEmail());
		userDetails.setPhoneNumber(user.getPersonnel().getPhoneNumber());
		userDetails.setDepartment(user.getPersonnel().getRole().getDisplayName());
		userDetails.setUserRoles(roles);
		userDetails.setCreatedAt(String.valueOf(user.getCreatedAt()));

		response.setUserDetails(userDetails);
		response.setSession(session.getSessionId());
		response.setToken(tokenHash);
		return ResponseEntity.ok().body(response);

	}

//Password change
	public ResponseEntity<PasswordResponse> passwordChange(UUID userId, PasswordRequest passwordRequest)
			throws Exception {

		RequestValidator.ValidatePassword(passwordRequest);

		userDao.changePassword(userId, passwordRequest.getCurrentPassword(), passwordRequest.getNewPassword());

		PasswordResponse passwordResponse = new PasswordResponse();
		passwordResponse.setStatus("SUCCESS");
		passwordResponse.setMessgae("User details updated successfully");
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

//session
	public void logout(UUID sessionId) {
		sessionDao.logout(sessionId);
	}

	// User List
	public ResponseEntity<UserListResponse> getUserList(AuthenticateRequest request) throws Exception {

		RequestValidator.validateUserListrequest(request);

		List<User> users = userDao.getUserList(request.getEmail(), request.getUsername());

		UserListResponse userListResponse = new UserListResponse();
		userListResponse.setStatus("SUCCESS");
		ArrayList<UserDetailsView> userList = new ArrayList<UserDetailsView>();
		UserDetailsView userDetail;
		List<UserRole> userRoles;
		for (User user : users) {
			userDetail = new UserDetailsView();

			userDetail.setUserId(user.getUserId());
			userDetail.setFirstName(user.getPersonnel().getFirstName());
			userDetail.setLastName(user.getPersonnel().getLastName());
			userDetail.setPhoneNumber(user.getPersonnel().getPhoneNumber());
			userDetail.setEmail(user.getPersonnel().getEmail());
			userDetail.setDepartment(user.getPersonnel().getRole().getDisplayName());
			userDetail.setActive(user.isActive());
			userRoles = userRoleDao.getUserRole(user.getUserId());

			ArrayList<String> roles = new ArrayList<>();
			for (UserRole userRole : userRoles) {
				roles.add(userRole.getRole().getRoleName());
			}
			userDetail.setUserRoles(roles);

			userList.add(userDetail);
		}

		userListResponse.setUsers(userList);

		return ResponseEntity.ok().body(userListResponse);

	}

	// Add New user By Admin

	public ResponseEntity<CreateUserResponse> createUser(CreateUserRequest userRequest) throws Exception {

		Personnel personnel = new Personnel();
		personnel.setFirstName(userRequest.getFirstName());
		personnel.setLastName(userRequest.getLastName());
		personnel.setEmail(userRequest.getEmail());
		personnel.setPhoneNumber(userRequest.getPhoneNumber());

		Organization org = organizationDao.getOrganization("Trivandrum International Airport");

		personnel.setOrganization(org);
		personnel.setRole(PersonnelRole.ACCOUNTABLE_MANAGER); // single

		personnel.setActive(userRequest.isActive());

		User user = new User();
		user.setUsername(userRequest.getFirstName()); // make sure this exists on the DTO
		user.setEmail(userRequest.getEmail());
		user.setPasswordHash(userRequest.getPassword());
		user.setOrganization(org);

		user.setPersonnel(personnel);
		user.setActive(userRequest.isActive());
		user.setCreatedAt(LocalDateTime.now()); // required, not-null column

		User userResp = userDao.createUser(user);

		CreateUserResponse response = UserMapper.mapCreateUserResponse(userResp);

		return ResponseEntity.ok().body(response);
	}

//	User Profile update By Admin

	public ResponseEntity<PasswordResponse> updateUserByAdmin(UUID userId,
			UserProfileUpadateRequest profileUpdateRequest) throws Exception {

		User userTobeUpdated = userDao.getUserbyUserId(userId);

		System.out.println(userTobeUpdated.getEmail());
		System.out.println(userTobeUpdated.getPersonnel().getFirstName());

		if (StringUtils.isNotBlank(profileUpdateRequest.getEmail())
				&& !profileUpdateRequest.getEmail().equalsIgnoreCase(userTobeUpdated.getEmail())) {

			userDao.checkUserEmailExists(profileUpdateRequest.getEmail());

			userTobeUpdated.setEmail(profileUpdateRequest.getEmail());
			userTobeUpdated.getPersonnel().setEmail(profileUpdateRequest.getEmail());
		}
		if (StringUtils.isNotBlank(profileUpdateRequest.getFirstName())) {
			userTobeUpdated.getPersonnel().setFirstName(profileUpdateRequest.getFirstName());
		}

		if (StringUtils.isNotBlank(profileUpdateRequest.getLastName())) {
			userTobeUpdated.getPersonnel().setLastName(profileUpdateRequest.getLastName());
		}

		if (StringUtils.isNotBlank(profileUpdateRequest.getPhoneNumber())) {
			userTobeUpdated.getPersonnel().setPhoneNumber(profileUpdateRequest.getPhoneNumber());
		}

		if (StringUtils.isNotBlank(profileUpdateRequest.getPassword())) {
			userTobeUpdated.setPasswordHash(profileUpdateRequest.getPassword());
		}

		personnelDao.checkUserPhoneNumberExists(userTobeUpdated.getPersonnel().getPersonId(),
				userTobeUpdated.getPersonnel().getPhoneNumber());

//		userDao.checkUserEmailExists(userTobeUpdated.getUserId(),userTobeUpdated.getEmail());

		userDao.updatUser(userTobeUpdated);

		PasswordResponse passwordResponse = new PasswordResponse();
		passwordResponse.setStatus("SUCCESS");
		passwordResponse.setMessgae("user profile updated successfully");
		return ResponseEntity.ok().body(passwordResponse);

	}
	// Delete User By Admin

	public ResponseEntity<PasswordResponse> deleteUser(UUID userId) throws Exception {

		userDao.deleteUser(userId);

		PasswordResponse passwordResponse = new PasswordResponse();
		passwordResponse.setStatus("SUCCESS");
		passwordResponse.setMessgae("user profile deleted successfully");
		return ResponseEntity.ok().body(passwordResponse);

	}

}
