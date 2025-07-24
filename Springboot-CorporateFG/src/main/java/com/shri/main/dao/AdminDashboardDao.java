package com.shri.main.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shri.main.model.Admin;
@Repository
public interface AdminDashboardDao extends JpaRepository<Admin, Integer> {
	@Query("SELECT COUNT(a) FROM Admin a")
	Integer getTotalAdmins();
}
