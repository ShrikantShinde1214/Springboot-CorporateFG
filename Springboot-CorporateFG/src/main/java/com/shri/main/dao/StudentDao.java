package com.shri.main.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shri.main.model.Student;

@Repository
public interface StudentDao extends JpaRepository<Student, Long> {
	List<Student> findAll();

	List<Student> findByWelcomeMessageSentFalse();

	List<Student> findByIsBirthdayWishSentFalse();

	// Added by Shrikant Shinde Query for manually send fees receipt [START HERE]
	@Query("SELECT s FROM Student s WHERE UPPER(s.name) LIKE UPPER(CONCAT('%', :name, '%'))")
	List<Student> findByNameContainingIgnoreCase(String name);

	List<Student> findByNameContainingIgnoreCaseAndBatchNo(String name, String batchNo);

	List<String> findDistinctBatchNoByOrderByBatchNoAsc();

	List<Student> findByBatchNo(String batchNo);

	Optional<Student> findById(int id);
	// Added by Shrikant Shinde Query for manually send fees receipt [END HERE]

}