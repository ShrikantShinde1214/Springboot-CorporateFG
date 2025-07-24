package com.shri.main.service.impl;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shri.main.dao.CourseCompletionDAO;
import com.shri.main.model.CourseCertManuallySend;
import com.shri.main.model.Student;
import com.shri.main.service.CourseCertManuallySendGenerator;
import com.shri.main.util.EmailUtil;

import jakarta.transaction.Transactional;

@Service
public class CourseCompletionServiceImpl implements CourseCompletionService {

    @Autowired
    private CourseCompletionDAO dao;

    @Autowired
    private CourseCertManuallySendGenerator certificateGenerator;

    @Autowired
    private EmailUtil emailUtil;

    @Override
    @Transactional
    public void processCourseCompletion(CourseCertManuallySend completion) {
        if (completion.isCertificateSent()) {
            Student student = new Student();
            student.setName(completion.getName());
            student.setEmail(completion.getEmail());
            Date from = Date.valueOf(completion.getFromDate());
            Date to = Date.valueOf(completion.getToDate());
            student.setAdmissionDate(from);

            try {
                byte[] pdf = certificateGenerator.generateCourseCompletionCertificate(student, from, to);
                emailUtil.sendEmailWithAttachmentManuallyCertSend(
                        completion.getEmail(),
                        "FG Infotech Course Completion Certificate",
                        "Dear " + completion.getName() + ",\n\nPlease find your course completion certificate attached.\n\nRegards,\nFG Infotech.\nWeb: https://fginfotech.in",
                        pdf,
                        "Course_Completion_Certificate.pdf"
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        dao.saveCourseCompletion(completion);
    }


}
