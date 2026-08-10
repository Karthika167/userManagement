package com.avio.dao.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.avio.dao.model.Personnel;

import jakarta.transaction.Transactional;

@Repository
public interface PersonalRepository extends JpaRepository<Personnel, UUID> {

	@Modifying
	@Transactional
	@Query("UPDATE Personnel p SET p.firstName=:firstName, p.lastName=:lastName, p.phoneNumber=:phoneNumber WHERE p.email = :email")
	int updateUser(@Param("email") String email, @Param("firstName") String firstName,
			@Param("lastName") String lastName, @Param("phoneNumber") String phoneNumber,
			@Param("department") String department);

}
