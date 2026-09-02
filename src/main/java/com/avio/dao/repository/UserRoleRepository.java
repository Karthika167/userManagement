package com.avio.dao.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.avio.dao.model.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID>{
	
	  
	
	 List<UserRole> findByUser_UserId(UUID userId);
	 @Modifying
	    @Query("DELETE FROM UserRole ur WHERE ur.user.userId = :userId")
	    void deleteByUserId(@Param("userId") UUID userId);

//	    @Modifying
//	    @Query("UPDATE UserRole ur SET ur.assignedBy.userId = :fallbackUserId WHERE ur.assignedBy.userId = :userId")
//	    void reassignAssignedBy(@Param("userId") UUID userId, @Param("fallbackUserId") UUID fallbackUserId);

}
