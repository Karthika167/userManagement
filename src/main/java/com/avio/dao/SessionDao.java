package com.avio.dao;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.avio.dao.model.Session;
import com.avio.dao.model.User;
import com.avio.dao.repository.SessionRepository;

import jakarta.transaction.Transactional;

@Repository
public class SessionDao {
	
	@Autowired
	private SessionRepository sessionRepository;
	
	@Transactional
	public Session createSession(User user, String tokenHash, String ipAddress, String userAgent) {
	    
		Session session = new Session();
	    session.setUser(user);
	    session.setTokenHash(tokenHash);
	    session.setIpAddress(ipAddress);
	    session.setUserAgent(userAgent);
	    session.setCreatedAt(LocalDateTime.now());
	    session.setExpiresAt(LocalDateTime.now().plusHours(2)); 

	    return sessionRepository.save(session);
	   

}
	@org.springframework.transaction.annotation.Transactional
	public void logout(UUID sessionId) {
	   
	    int rows = sessionRepository.revokeSession(sessionId, LocalDateTime.now());
	    System.out.println("Rows updated: " + rows);
	}
	@Transactional
	public void deleteByUserId(UUID userId) {
		sessionRepository.deleteByUser_UserId(userId);
		
	}
	
	
	
	
	
}