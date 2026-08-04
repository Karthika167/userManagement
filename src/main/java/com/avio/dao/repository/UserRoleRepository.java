package com.avio.dao.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.avio.dao.model.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID>{
	
	 List<UserRole> findByUser_UserId(UUID userId);

}
