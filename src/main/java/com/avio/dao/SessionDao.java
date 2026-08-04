package com.avio.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.avio.dao.repository.SessionRepository;

@Repository
public class SessionDao {
	
	@Autowired
	private SessionRepository sessionRepository;
	
	

}
