package com.avio.dao;

import java.time.LocalDateTime;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.avio.dao.model.Personnel;
import com.avio.dao.model.User;
import com.avio.dao.repository.UserRepository;
import com.avio.view.UserUpadateRequest;

@Repository
public class UserDao {

	@Autowired
	private UserRepository userRepository;

	@Transactional
	public User authenticateUser(String username, String password, String email) throws Exception {

		User user = null;

		if (StringUtils.isNotBlank(username)) {
			user = userRepository.findByUsernameAndPasswordHash(username, password);
		} else {
			user = userRepository.findByEmailAndPasswordHash(email, password);
		}

		if (user == null) {
			throw new Exception("Invalid Username or Password");
		}

		userRepository.updateLastLogin(user.getUserId(), LocalDateTime.now());

		return user;
	}

	@Transactional
	public User changePassword(String email, String username, String currentPassword, String newPassword)
			throws Exception {

		User user = null;
		if (StringUtils.isNotBlank(username)) {
			System.out.println("username");
			user = userRepository.findByUsernameAndPasswordHash(username, currentPassword);
		} else {

			System.out.println("inside else");
			user = userRepository.findByEmailAndPasswordHash(email, currentPassword);
		}
		if (user == null) {
			throw new Exception("invalid password, give correct password");
		}

		userRepository.updatePasswordHash(user.getUserId(), newPassword);

		return user;
	}

}
