package com.avio.dao.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.avio.dao.model.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID>{
	
	
}

