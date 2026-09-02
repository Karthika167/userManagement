package com.avio.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.avio.dao.model.Organization;
import com.avio.dao.model.UserRole;
import com.avio.dao.repository.OrganizationRepository;
import com.avio.dao.repository.UserRoleRepository;



@Repository
public class OrganizationDao {

	@Autowired
	private OrganizationRepository organizationRepository ;

	@Transactional
	public Organization getOrganization(UUID orgId) throws Exception {

		return organizationRepository.getReferenceById(orgId);
	
	}


}
	