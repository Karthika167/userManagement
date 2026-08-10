package com.avio.validator;

import org.apache.commons.lang3.StringUtils;

import com.avio.view.AuthenticateRequest;
import com.avio.view.PasswordRequest;
import com.avio.view.UserUpadateRequest;

public class RequestValidator {

	public static void validateAuthenticaterequest(AuthenticateRequest request) throws Exception {

		if (StringUtils.isBlank(request.getUsername())&& StringUtils.isBlank(request.getEmail())) {
			throw new Exception("Missing Mandatory Parameter:-  Username/emailId");
		}

		if (StringUtils.isBlank(request.getPassword())) {
			throw new Exception("Missing Mandatory Parameter:-  password");
		
		

		}
		
	}

	public static void ValidatePassword(PasswordRequest passwordRequest) throws Exception {
		
		
		if (StringUtils.isBlank(passwordRequest.getCurrentPassword())) {
	        throw new Exception("Missing Mandatory Parameter: currentPassword");
	    }
	    if (StringUtils.isBlank(passwordRequest.getNewPassword())) {
	        throw new Exception("Missing Mandatory Parameter: newPassword");
	    }
	    
	    if (passwordRequest.getCurrentPassword().equals(passwordRequest.getNewPassword())) {
	        throw new Exception("New password must be different from current password");
	    }
	}

	public static void ValidateUserUpdate(UserUpadateRequest userUpadateRequest) throws Exception {
		
		ValidateMandatoryParam(userUpadateRequest.getFirstName(), "First Name");
		ValidateMandatoryParam(userUpadateRequest.getLastName(), "Last Name");
		ValidateMandatoryParam(userUpadateRequest.getPhoneNumber(), "Phnone Number");
		//ValidateMandatoryParam(userUpadateRequest.getDepartment(), "Department");
		
		
		
	}

	private static void ValidateMandatoryParam(String value, String fieldName) throws Exception {
		if (StringUtils.isBlank(value)) {
	        throw new Exception("Missing Mandatory Parameter: " + fieldName);
	    }
	}
	
		


}
