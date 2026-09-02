package com.avio.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.avio.dao.RoleDao;
import com.avio.dao.model.Role;
import com.avio.view.RoleListResponse;
import com.avio.view.RoleView;

@Service
public class RoleService {

	@Autowired
	RoleDao roleDao;

	public ResponseEntity<?> getroles(UUID orgId) {

		List<Role> roles = roleDao.getRoles(orgId);

		RoleListResponse roleListResponse = new RoleListResponse();
		
		roleListResponse.setStatus("Success");
		ArrayList<RoleView> roleList = new ArrayList<RoleView>();

		RoleView r;
		for (Role role : roles) {
			r = new RoleView();

			r.setRoleId(role.getRoleId());
			r.setRoleName(role.getRoleName());

			roleList.add(r);
		}

		roleListResponse.setRole(roleList);
		return ResponseEntity.ok().body(roleListResponse);
	}

}
