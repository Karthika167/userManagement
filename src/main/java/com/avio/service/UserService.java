package com.avio.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.avio.dao.OrganizationDao;
import com.avio.dao.PersonnelDao;
import com.avio.dao.RoleDao;
import com.avio.dao.SessionDao;
import com.avio.dao.UserDao;
import com.avio.dao.UserRoleDao;
import com.avio.dao.model.Organization;
import com.avio.dao.model.Role;
import com.avio.dao.model.Session;
import com.avio.dao.model.User;
import com.avio.dao.model.UserRole;
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

@Service
public class UserService {

	@Autowired
	UserDao userDao;

	@Autowired

	SessionDao sessionDao;

	@Autowired
	UserRoleDao userRoleDao;

	@Autowired
	RoleDao roleDao;

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

		// String tokenHash = "this is token";
		String tokenHash = jwtUtil.generateToken(user.getUserId(), user.getEmail(), user.getOrganization().getOrgId(),
				List.of()); // roles empty list for
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
		userDetails.setUserRoles(UserMapper.mapUserRole(userRoles));
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
	public ResponseEntity<PasswordResponse> updateUser(UUID userId, UserUpadateRequest userUpadateRequest)
			throws Exception {

		User profileUpdate = userDao.findById(userId).orElseThrow(() -> new Exception("User not found: " + userId));


		if (StringUtils.isNotBlank(userUpadateRequest.getPhoneNumber()) && !Strings.CI
				.equals(userUpadateRequest.getPhoneNumber(), profileUpdate.getPersonnel().getPhoneNumber())) {

//			System.out.println(userUpadateRequest.getPhoneNumber());

			personnelDao.checkUserPhoneNumberExists(userUpadateRequest.getPhoneNumber());
		}

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

	// User List view by Admin
	public ResponseEntity<UserListResponse> getUserList(AuthenticateRequest request) throws Exception {

		// RequestValidator.validateUserListrequest(request);

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

			userDetail.setUserRoles(UserMapper.mapUserRole(userRoles));

			userList.add(userDetail);
		}

		userListResponse.setUsers(userList);

		return ResponseEntity.ok().body(userListResponse);

	}

	// Add New user By Admin

	public ResponseEntity<CreateUserResponse> createUser(CreateUserRequest userRequest, UUID orgId, UUID logedInUserId)
			throws Exception {

		personnelDao.checkUserDetails(userRequest); // check email and phone Number existing

		Organization org = organizationDao.getOrganization(orgId);
		User loggedInUser = userDao.getUserbyUserId(logedInUserId);

		User user = UserMapper.mapCreateUser(userRequest, org);
		user = userDao.createUser(user); // <-- persist first, capture the managed/returned entity

//create user role
		List<UserRole> userRoles = new ArrayList<UserRole>();
		UserRole userRole;
		Role role;
		for (UUID roleId : userRequest.getRoles()) {
//declare the variables userRole and role
			role = roleDao.getRoleByRoleId(roleId);
			
			userRole = new UserRole();
			
			// set user role table
			userRole.setUser(user);
			userRole.setRole(role);
			userRole.setOrganization(org);
			userRole.setAssignedBy(loggedInUser);
			userRole.setAssignedAt(LocalDateTime.now());

			userRoles.add(userRole);
		}

		userRoleDao.createUserRole(userRoles);

//		User userResp = new User();
//		CreateUserResponse response = UserMapper.mapCreateUserResponse(userResp, userRoles);
		CreateUserResponse response = UserMapper.mapCreateUserResponse(user, userRoles);

		return ResponseEntity.ok().body(response);
	}

//	User Profile update By Admin

	public ResponseEntity<PasswordResponse> updateUserByAdmin(UUID userId,
			UserProfileUpadateRequest profileUpdateRequest) throws Exception {

		User userTobeUpdated = userDao.getUserbyUserId(userId);

		if (!Strings.CI.equals(userTobeUpdated.getPersonnel().getPhoneNumber(),
				profileUpdateRequest.getPhoneNumber())) {
			personnelDao.checkUserPhoneNumberExists(profileUpdateRequest.getPhoneNumber());
		}

		if (!Strings.CI.equals(userTobeUpdated.getEmail(), profileUpdateRequest.getEmail())) {
			userDao.checkUserEmailExists(profileUpdateRequest.getEmail());
		}

//		System.out.println(userTobeUpdated.getEmail());
//		System.out.println(userTobeUpdated.getPersonnel().getFirstName());

		if (StringUtils.isNotBlank(profileUpdateRequest.getEmail())
				&& !profileUpdateRequest.getEmail().equalsIgnoreCase(userTobeUpdated.getEmail())) {

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
//		System.out.println("is the user active: " + profileUpdateRequest.isActive());
		userTobeUpdated.setActive(profileUpdateRequest.isActive());

		userDao.updatUser(userTobeUpdated);

		PasswordResponse passwordResponse = new PasswordResponse();
		passwordResponse.setStatus("SUCCESS");
		passwordResponse.setMessgae("user profile updated successfully");
		return ResponseEntity.ok().body(passwordResponse);

	}

	// Delete User By Admin

	public ResponseEntity<PasswordResponse> deleteUser(UUID userId) throws Exception {

		userDao.findById(userId).orElseThrow(() -> new Exception("User not found: " + userId));
		
		sessionDao.deleteByUserId(userId);// remove session of this user
		

		userRoleDao.deleteByUserId(userId); // removes roles assigned to this user

		userDao.deleteUser(userId);// remove the user from user table

		PasswordResponse passwordResponse = new PasswordResponse();
		passwordResponse.setStatus("SUCCESS");
		passwordResponse.setMessgae("user profile deleted successfully");
		return ResponseEntity.ok().body(passwordResponse);

	}

}
