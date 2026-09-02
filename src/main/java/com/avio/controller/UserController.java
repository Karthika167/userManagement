package com.avio.controller;

import java.util.UUID;

//import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.avio.service.RoleService;
import com.avio.service.UserService;
import com.avio.util.JwtUtil;
import com.avio.view.AuthenticateRequest;
import com.avio.view.CreateUserRequest;
import com.avio.view.PasswordRequest;
import com.avio.view.UserProfileUpadateRequest;
import com.avio.view.UserUpadateRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping(value = "/avio/user")
//@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	RoleService roleService;

	@Autowired
	JwtUtil jwtUtil;
 // login
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> authenticate(HttpServletRequest httpRequest, @RequestBody AuthenticateRequest request)
			throws Exception {

		String ipAddress = httpRequest.getHeader("X-Forwarded-For");
		if (ipAddress == null || ipAddress.isEmpty()) {
			ipAddress = httpRequest.getRemoteAddr();
		}

		String userAgent = httpRequest.getHeader("User-Agent");
		return userService.authenticateUser(request, ipAddress, userAgent);

	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public ResponseEntity<?> logout(@RequestParam UUID sessionId) {
		userService.logout(sessionId);
		return ResponseEntity.ok().body("Logged out successfully");
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public ResponseEntity<?> passwordChange(@RequestBody PasswordRequest passwordRequest,
			@RequestHeader("Authorization") String authHeader) throws Exception {

		String token = authHeader.replace("Bearer ", "");
		if (!jwtUtil.isTokenValid(token)) {
			return ResponseEntity.status(401).body("Invalid or expired token");
		}

		UUID userId = jwtUtil.extractUserId(token);
		return userService.passwordChange(userId, passwordRequest);

	}

	// update user profile
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public ResponseEntity<?> updateUser(@RequestBody UserUpadateRequest userUpadateRequest,
			@RequestHeader("Authorization") String authHeader) throws Exception {

		String token = authHeader.replace("Bearer ", "");
		if (!jwtUtil.isTokenValid(token)) {
			return ResponseEntity.status(401).body("Invalid or expired token");
		}

		UUID userId = jwtUtil.extractUserId(token);

		return userService.updateUser(userId, userUpadateRequest);

	}
//user list

	@RequestMapping(value = "/getUsers", method = RequestMethod.POST)
	public ResponseEntity<?> getUsers(@RequestBody AuthenticateRequest request) throws Exception {

		return userService.getUserList(request);

	}

	@RequestMapping(value = "/createUser", method = RequestMethod.PUT)
	public ResponseEntity<?> createUser(@RequestBody CreateUserRequest userRequest,
			@RequestHeader("Authorization") String authHeader) throws Exception {

		String token = authHeader.replace("Bearer ", "");
		if (!jwtUtil.isTokenValid(token)) {
			return ResponseEntity.status(401).body("Invalid or expired token");
		}

		return userService.createUser(userRequest, jwtUtil.extractOrgId(token), jwtUtil.extractUserId(token));

	}

//	update user by Admin
	@RequestMapping(value = "/{userId}", method = RequestMethod.PATCH)
	public ResponseEntity<?> updateUserByAdmin(@PathVariable UUID userId,
			@RequestBody UserProfileUpadateRequest profileUpdateRequest) throws Exception {

//		String token = authHeader.replace("Bearer ", "");
//		if (!jwtUtil.isTokenValid(token)) {
//			return ResponseEntity.status(401).body("Invalid or expired token");
//		}
//
//		UUID callingAdminId = jwtUtil.extractUserId(token);
		return userService.updateUserByAdmin(userId, profileUpdateRequest);
	}

//delete user in user management
	@RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteUser(@PathVariable UUID userId) throws Exception {

		return userService.deleteUser(userId);

	}

	@RequestMapping(value = "/roles", method = RequestMethod.GET)
	public ResponseEntity<?> getRoles(@RequestHeader("Authorization") String authHeader) throws Exception {

		String token = authHeader.replace("Bearer ", "");
		if (!jwtUtil.isTokenValid(token)) {
			return ResponseEntity.status(401).body("Invalid or expired token");
		}

		return roleService.getroles(jwtUtil.extractOrgId(token));

	}

}
