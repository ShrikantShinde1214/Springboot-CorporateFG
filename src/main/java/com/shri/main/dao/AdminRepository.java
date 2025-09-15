package com.shri.main.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.shri.main.model.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
	//Optional<Admin> findByUsernameAndPassword(String username, String password);
	Optional<Admin> findByUsername(String username);


}
