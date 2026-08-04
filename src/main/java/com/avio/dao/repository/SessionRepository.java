package com.avio.dao.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.avio.dao.model.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID>{
	
	
	
	// for logout
	@Modifying
    @Query("UPDATE Session s SET s.revokedAt = :revokedAt WHERE s.sessionId = :sessionId")
    int revokeSession(@Param("sessionId") UUID sessionId, @Param("revokedAt") LocalDateTime revokedAt);
	
	// Revoke all sessions for a user (e.g., "log out everywhere")
    @Modifying
    @Query("UPDATE Session s SET s.revokedAt = :revokedAt WHERE s.user.userId = :userId AND s.revokedAt IS NULL")
    int revokeAllSessionsForUser(@Param("userId") UUID userId, @Param("revokedAt") LocalDateTime revokedAt);
	
	
}

