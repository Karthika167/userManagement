package com.avio.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.avio.dao.model.Role;
import com.avio.dao.repository.RoleRepository;

@Repository
public class RoleDao {

	@Autowired
	RoleRepository roleRepository;
	
	public List<Role> getRoles(UUID orgId) {
		
		return roleRepository.findByOrganization_OrgId(orgId);
	}

	public  Role getRoleByRoleId(UUID roleId) {
		
		return roleRepository.getReferenceById(roleId);
	}

	
	
}
