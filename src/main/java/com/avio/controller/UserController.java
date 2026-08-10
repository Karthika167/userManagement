package com.avio.controller;

import java.util.UUID;

//import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.avio.service.UserService;
import com.avio.view.AuthenticateRequest;
import com.avio.view.PasswordRequest;
import com.avio.view.UserUpadateRequest;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/avio/user")
//@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

	@Autowired
	UserService userService;

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
//	
	
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public ResponseEntity<?> passwordChange(@RequestBody PasswordRequest passwordRequest) throws Exception {

		return userService.passwordChange(passwordRequest);

	}

	//update user
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public ResponseEntity<?> updateUser(@RequestBody UserUpadateRequest userUpadateRequest) throws Exception {

		return userService.updateUser(userUpadateRequest);

	}
}
