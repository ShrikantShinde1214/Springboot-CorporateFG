package com.shri.main.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shri.main.model.Student;

import jakarta.transaction.Transactional;

@Repository
public interface StudentDashboardDao extends JpaRepository<Student, Integer> {

	@Query("SELECT COUNT(s) FROM Student s")
	Integer getTotalStudents();

	@Query("SELECT COUNT(s) FROM Student s WHERE s.paidFees >= 2000")
	Integer getPaidStudents();

	@Query("SELECT COUNT(s) FROM Student s WHERE s.placed = true")
	Integer getPlacedStudents();

	@Query("SELECT COUNT(DISTINCT s.course) FROM Student s")
	Integer getAllCourses();

	@Query("SELECT COUNT(s) FROM Student s WHERE s.paidFees = 40000 OR s.paidFees=45000")
	Integer getAllTFeesNill();

	@Query("SELECT COUNT(*) FROM Student s WHERE s.paidFees=1")
	Integer getReceivedFees();

	@Query("SELECT COALESCE(SUM(s.paidFees), 0) FROM Student s WHERE s.paidFees >= 2000")
	Integer getReceivedFees11();

	@Query("SELECT COUNT(DISTINCT s.batchNo) FROM Student s")
	Integer getAllBatches();

	@Query("SELECT COALESCE(SUM(s.totalFees), 0) FROM Student s WHERE s.paidFees != 1")
	Float getTotalFees();

	@Query("SELECT COALESCE(SUM(s.totalFees - s.paidFees), 0) FROM Student s WHERE s.paidFees != 1")
	Integer getPendingFees();

	@Query("SELECT COALESCE(SUM(s.totalFees), 0) FROM Student s WHERE s.paidFees=1")
	Integer getScholarships();

	Student findByName(String name);

	@Transactional
	@Modifying
	@Query("UPDATE Student s SET s.placed = true WHERE s.name = :name AND s.placed = false")
	int markStudentAsPlaced(@Param("name") String name);

	@Query("SELECT s FROM Student s WHERE s.placed = true")
	List<Student> findByPlacedTrue();

	// Get student by name
	@Query("SELECT s FROM Student s WHERE s.name = :name")
	Student getByName(String name);
}
