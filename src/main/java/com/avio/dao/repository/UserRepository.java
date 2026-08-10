package com.avio.dao.repository;

import java.time.LocalDateTime;
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

	@Modifying
	@Query("UPDATE User u SET u.lastLoginAt = :lastLoginAt WHERE u.userId = :userId")
	int updateLastLogin(@Param("userId") UUID userId, @Param("lastLoginAt") LocalDateTime locaLocalDateTime);

	@Modifying
	@Transactional
	@Query("UPDATE User u SET u.passwordHash = :newPassword WHERE u.userId = :userId")
	int updatePasswordHash(@Param("userId") UUID userId, @Param("newPassword") String newPassword);

}

//@Modifying
//@Transactional
//@Query("UPDATE User u SET u.passwordHash = :newPasswordHash, u.updatedAt = :updatedAt WHERE u.userId = :userId")
//int updatePasswordHash(@Param("userId") UUID userId,
//                        @Param("newPasswordHash") String newPasswordHash,
//                        @Param("updatedAt") LocalDateTime updatedAt);