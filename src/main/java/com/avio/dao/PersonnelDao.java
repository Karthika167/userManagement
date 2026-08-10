package com.avio.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.avio.dao.repository.PersonalRepository;
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

}
