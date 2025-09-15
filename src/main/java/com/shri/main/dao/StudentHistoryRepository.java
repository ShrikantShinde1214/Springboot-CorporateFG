package com.shri.main.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.shri.main.model.StudentHistory;

public interface StudentHistoryRepository extends JpaRepository<StudentHistory, Long> {
    List<StudentHistory> findByStudentIdOrderByUpdatedOnDesc(Long studentId);
}
