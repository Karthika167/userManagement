package com.avio.dao.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.avio.dao.model.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

	User findByUsernameAndPasswordHash(String username, String passwordHash);

	User findByEmailAndPasswordHash(String email, String passwordHash);
	
	List<User> findByOrganization_OrgId(UUID orgId);

	User findByUsername(String username);

	User findByEmail(String email);
	
	Optional<User> findByUserId(UUID userId);
	

	@Modifying
	@Query("UPDATE User u SET u.lastLoginAt = :lastLoginAt WHERE u.userId = :userId")
	int updateLastLogin(@Param("userId") UUID userId, @Param("lastLoginAt") LocalDateTime locaLocalDateTime);

	@Modifying
	@Transactional
	@Query("UPDATE User u SET u.passwordHash = :newPassword WHERE u.userId = :userId")
	int updatePasswordHash(@Param("userId") UUID userId, @Param("newPassword") String newPassword);
	
	// --- update existence checks for create ---

	boolean existsByEmail(String email);

	



	

}

