package com.avio.dao.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.avio.dao.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    // Navigates Role -> Organization -> orgId (nested property via underscore notation)
    List<Role> findByOrganization_OrgId(UUID orgId);

    // Optional: exclude system roles if you ever need only custom/org-defined roles
    List<Role> findByOrganization_OrgIdAndIsSystemRoleFalse(UUID orgId);
}