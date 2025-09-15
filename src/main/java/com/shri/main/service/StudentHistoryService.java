package com.shri.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shri.main.dao.StudentHistoryRepository;
import com.shri.main.model.StudentHistory;

@Service
public class StudentHistoryService {

    @Autowired
    private StudentHistoryRepository repo;

    public List<StudentHistory> getHistoryByStudentId(Long studentId) {
        return repo.findByStudentIdOrderByUpdatedOnDesc(studentId);
    }
}
