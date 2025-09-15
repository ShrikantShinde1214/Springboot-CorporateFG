package com.shri.main.service.impl;

import com.shri.main.dao.InternshipDAO;
import com.shri.main.model.Internship;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

@Repository
public class InternshipDAOImpl implements InternshipDAO {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void saveInternship(Internship internship) {
		entityManager.persist(internship);
	}

}
