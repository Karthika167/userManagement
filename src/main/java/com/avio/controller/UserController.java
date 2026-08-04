package com.avio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.avio.service.UserService;
import com.avio.view.AuthenticateRequest;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/avio/user")
@CrossOrigin(origins = "http://localhost:5173")
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

}
