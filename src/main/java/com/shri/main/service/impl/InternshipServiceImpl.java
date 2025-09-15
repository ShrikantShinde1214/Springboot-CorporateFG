package com.shri.main.service.impl;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shri.main.dao.InternshipDAO;
import com.shri.main.model.Internship;
import com.shri.main.model.Student;
import com.shri.main.service.InternshipCertificateGenerator;
import com.shri.main.util.EmailUtil;

import jakarta.transaction.Transactional;

@Service
public class InternshipServiceImpl implements InternshipService {

    @Autowired
    private InternshipDAO internshipDAO;

    @Autowired
    private InternshipCertificateGenerator certificateGenerator;

    @Autowired
    private EmailUtil emailUtil;

    @Override
    @Transactional
    public void processInternship(Internship internship) {
        if (internship.isCertificateSent()) {
            // Create student object
            Student student = new Student();
            student.setName(internship.getName());
            student.setEmail(internship.getEmail());
            student.setCourse(internship.getRole());

            // Convert LocalDate to java.sql.Date
            LocalDate fromLocalDate = internship.getFromDate();
            LocalDate toLocalDate = internship.getToDate();

            Date fromDate = Date.valueOf(fromLocalDate);
            Date toDate = Date.valueOf(toLocalDate);

            student.setAdmissionDate(fromDate); // Set fromDate as admissionDate

            try {
                // Pass both from and to dates to the generator
                byte[] pdf = certificateGenerator.generateInternshipCertificate(student, fromDate, toDate);

                // Send email with attachment
                emailUtil.sendEmailWithAttachment(
                        internship.getEmail(),
                        "FG Infotech Internship Certificate",
                        "Dear " + internship.getName() + ",\n\nPlease find your internship certificate attached.\n\nRegards,\nFG Infotech.\nWeb: https://fginfotech.in",
                         pdf,
                        "Internship_Certificate.pdf"
                );
            } catch (Exception e) {
                e.printStackTrace(); // You can add SLF4J logging
            }
        }

        // Save internship record to DB
        internshipDAO.saveInternship(internship);
    }
}
