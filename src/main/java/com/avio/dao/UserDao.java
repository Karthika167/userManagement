package com.avio.dao;
import com.avio.dao.repository.PersonalRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.avio.dao.model.User;
import com.avio.dao.repository.UserRepository;
import com.avio.view.UserProfileUpadateRequest;

@Repository
public class UserDao {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PersonalRepository personalRepository;


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
	public User changePassword(UUID userId, String currentPassword, String newPassword) throws Exception {

	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

	    if (!StringUtils.equals(currentPassword, user.getPasswordHash())) {
	    	System.out.println("inside incorrect pwd");
	        throw new Exception("The current password you entered is incorrect.");
	    }
	    System.out.println(user.getEmail());
	    user.setPasswordHash(newPassword);

	    return userRepository.save(user);
	}

	@Transactional
	public List<User> getUserList(String email, String username) throws Exception {

		User user = null;
		if (StringUtils.isNotBlank(username)) {
//			System.out.println("username");
			user = userRepository.findByUsername(username);
		} else {

//			System.out.println("inside else");
			user = userRepository.findByEmail(email);
		}
		if (user == null) {
			throw new Exception("User not found for given emai Id.");
		}

		return userRepository.findByOrganization_OrgId(user.getOrganization().getOrgId());

	}
// create user by Admin
	@Transactional
	public User createUser(User user) throws Exception{
		

		
		    return userRepository.save(user);
	}



	@Transactional
	public User getUserbyUserId(UUID userId) {
		
		
		Optional<User> user = userRepository.findById(userId);

		return user.get(); // means ?
	}

	
	@Transactional
	public void updatUser(User userTobeUpdated) throws Exception {
		

		
  	userRepository.save(userTobeUpdated);
		

	}
	
	

	@Transactional
	public void deleteUser(UUID userId) {
		
		userRepository.deleteById(userId);
		
	}
	@Transactional
	public void checkUserEmailExists(String email) throws Exception {
		

		if (userRepository.existsByEmail(email)) {
			
		
		        throw new Exception("A user with this email already exists.");
			    }
//		
	}

	public Optional<User> findById(UUID userId) {
		return userRepository.findById(userId);
	}


		
		 
	}

	
	


