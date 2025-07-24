package com.shri.main.service.impl;

import org.springframework.stereotype.Repository;

import com.shri.main.dao.CourseCompletionDAO;
import com.shri.main.model.CourseCertManuallySend;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class CourseCompletionDAOImpl implements CourseCompletionDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveCourseCompletion(CourseCertManuallySend courseCompletion) {
        entityManager.persist(courseCompletion);
    }
}
