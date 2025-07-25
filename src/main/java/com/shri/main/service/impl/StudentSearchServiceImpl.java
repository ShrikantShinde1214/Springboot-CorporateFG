package com.shri.main.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shri.main.dao.StudentDao;
import com.shri.main.model.Student;

@Service
public class StudentSearchServiceImpl implements StudentSearchService {

	@Autowired
	private StudentDao studentDao;

	@Override
	public List<Student> findByName(String name) {
		return studentDao.findByNameContainingIgnoreCase(name);
	}

	@Override
	public List<Student> findByNameOrBatch(String name, String batch) {
		if (name != null && !name.isBlank()) {
			return studentDao.findByNameContainingIgnoreCase(name);
		} else if (batch != null && !batch.isBlank()) {
			return studentDao.findByBatchNo(batch);
		}
		return studentDao.findAll();
	}

	@Override
	public List<String> findAllBatchNos() {
		return studentDao.findAll().stream().map(Student::getBatchNo).distinct().collect(Collectors.toList());
	}

	@Override
	public Student getStudentById(int id) {
		return studentDao.findById(id).orElse(null);
	}
}
