package com.avio.dao;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.avio.dao.model.Personnel;
import com.avio.dao.model.User;
import com.avio.dao.repository.PersonalRepository;
import com.avio.view.CreateUserRequest;
import com.avio.view.UserProfileUpadateRequest;
import com.avio.view.UserUpadateRequest;

@Repository
public class PersonnelDao {

	@Autowired
	private PersonalRepository personalRepository;

	@Transactional
	public void updateUser(UserUpadateRequest userUpadateRequest) {

		personalRepository.updateUser(userUpadateRequest.getEmail(), userUpadateRequest.getFirstName(),
				userUpadateRequest.getLastName(), userUpadateRequest.getPhoneNumber(),
				userUpadateRequest.getDepartment());

	}

	@Transactional
	public void Update(UserUpadateRequest userUpadateRequest) {

		personalRepository.updateUser(userUpadateRequest.getEmail(), userUpadateRequest.getFirstName(),
				userUpadateRequest.getLastName(), userUpadateRequest.getPhoneNumber(),
				userUpadateRequest.getDepartment());

	}

	
	@Transactional
	public void checkUserPhoneNumberExists(String phoneNumber) throws Exception {
		
		if (personalRepository.existsByPhoneNumber(phoneNumber)) {

			throw new Exception("A user with this phone number already exists.");
		}

		
	}

	public void checkUserDetails(CreateUserRequest userRequest) throws Exception {
		 if (personalRepository.existsByEmail(userRequest.getEmail())) {
		        throw new Exception("A user with this email already exists.");
		    }

		    
		    if ( personalRepository.existsByPhoneNumber(userRequest.getPhoneNumber())) {
		        throw new Exception("A user with this phone number already exists.");
		    }

	}

}
