package com.avio.dao.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.avio.dao.model.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID>{

	Organization findByName(String name);
	
}
