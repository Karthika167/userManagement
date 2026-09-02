package com.avio.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.avio.dao.model.UserRole;
import com.avio.dao.repository.UserRoleRepository;



@Repository
public class UserRoleDao {

	@Autowired
	private UserRoleRepository userRoleRepository;

	@Transactional
	public List<UserRole> getUserRole(UUID userId) throws Exception {

		return userRoleRepository.findByUser_UserId(userId);
	
	}
	
	@Transactional
	public List<UserRole> createUserRole(List<UserRole> userRoles) throws Exception {

		return userRoleRepository.saveAll(userRoles);
	
	}

	 @Transactional
	    public void deleteByUserId(UUID userId) {
	        userRoleRepository.deleteByUserId(userId);
	    }

//	    @Transactional
//	    public void reassignAssignedBy(UUID userId, UUID fallbackUserId) {
//	        userRoleRepository.reassignAssignedBy(userId, fallbackUserId);
//	    }


}
	