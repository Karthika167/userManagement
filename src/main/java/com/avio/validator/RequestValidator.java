package com.avio.validator;

import org.apache.commons.lang3.StringUtils;

import com.avio.view.AuthenticateRequest;

public class RequestValidator {

	public static void validateAuthenticaterequest(AuthenticateRequest request) throws Exception {

		if (StringUtils.isBlank(request.getUsername())&& StringUtils.isBlank(request.getEmail())) {
			throw new Exception("Missing Mandatory Parameter:-  Username/emailId");
		}

		if (StringUtils.isBlank(request.getPassword())) {
			throw new Exception("Missing Mandatory Parameter:-  password");
		}
		

	}

}
