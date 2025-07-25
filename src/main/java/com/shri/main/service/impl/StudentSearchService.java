package com.shri.main.service.impl;

import com.shri.main.model.Student;
import java.util.List;

public interface StudentSearchService {
	List<Student> findByName(String name);

	List<Student> findByNameOrBatch(String name, String batch);

	List<String> findAllBatchNos();

	Student getStudentById(int id);
}
