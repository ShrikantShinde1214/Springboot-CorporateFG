package com.shri.main.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.shri.main.dao.AdminRepository;
import com.shri.main.dao.StudentDao;
import com.shri.main.model.Admin;
import com.shri.main.model.Student;
import com.shri.main.util.BirthdayUtil;

@Service
public class BirthdayWishSendScheduler {

    private static final Logger logger = LoggerFactory.getLogger(BirthdayWishSendScheduler.class);

    @Autowired
    private StudentDao studentRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AdminRepository adminRepository;

    //Student birthday check - runs daily at 8 AM
    @Scheduled(cron = "0 1 0 * * *")  // runs at 12:01 AM every day
    //@Scheduled(fixedDelay = 60000)
    public void runStudentBirthdayScheduler() {
        System.out.println("🎉 Student Birthday Scheduler running at: " + LocalDate.now());
        logger.info("🎉 Student Birthday Scheduler triggered.");
        sendBirthdayWishesToStudents();
    }

    //Admin birthday check - runs daily at 9 AM
    @Scheduled(cron = "0 1 0 * * *")  // runs at 12:01 AM every day
    //@Scheduled(fixedDelay = 60000)
    public void runAdminBirthdayScheduler() {
        System.out.println("🎉 Admin Birthday Scheduler running at: " + LocalDate.now());
        logger.info("🎉 Admin Birthday Scheduler triggered.");
        sendBirthdayWishesToAdmins();
    }

    //Send birthday wish to students once
    public void sendBirthdayWishesToStudents() {
        List<Student> students = studentRepo.findAll();
        logger.info("🔍 Checking {} students for birthday wishes.", students.size());

        for (Student student : students) {
            //if (!student.isBirthdayWishSent() && BirthdayUtil.shouldSendBirthdayWish(student)) {
            	if (!Boolean.TRUE.equals(student.getIsBirthdayWishSent()) && BirthdayUtil.shouldSendBirthdayWish(student)) {

                logger.info("🎂 Sending birthday wish to student: {}", student.getName());
                System.out.println("✅ Sending birthday wish to: " + student.getName());

                String message =
                        "🎉🎂 *Happy Birthday " + student.getName() + "!* 🎂🎉\n\n" +
                        "On your special day, we at *FG Infotech* wish you:\n" +
                        "✨ Success in all you do,\n" +
                        "🌟 Joy that fills your heart,\n" +
                        "📘 Knowledge that lights your path,\n" +
                        "💼 A future full of opportunities!\n\n" +
                        "🎁 May this year bring you even closer to your dreams.\n\n" +
                        "With warm regards,\n" +
                        "*Team FG Infotech* 💻\n" +
                        "🌐 Visit us: https://fginfotech.in";

                emailService.sendBirthdayWishMail(
                        student.getEmail(),
                        "🎉 Happy Birthday " + student.getName() + " 🎂",
                        message
                );

                student.setIsBirthdayWishSent(true);
                studentRepo.save(student);

                logger.info("✅ Birthday wish sent and updated for: {}", student.getEmail());
            } else {
                logger.info("⏩ Skipping student (not eligible or already wished): {}", student.getName());
            }
        }
    }

    //Send admin birthday wish once per year
    public void sendBirthdayWishesToAdmins() {
        LocalDate today = LocalDate.now();
        List<Admin> admins = adminRepository.findAll();
        logger.info("🔍 Checking {} admins for birthday wishes.", admins.size());

        for (Admin admin : admins) {
            LocalDate dob = admin.getDob();

            if (dob != null &&
                dob.getDayOfMonth() == today.getDayOfMonth() &&
                dob.getMonth() == today.getMonth() &&
                (admin.getLastBirthdayWishedAt() == null || admin.getLastBirthdayWishedAt().getYear() < today.getYear())
            ) {
                String name = admin.getUsername() != null ? admin.getUsername() : admin.getLoginId();
                logger.info("🎂 Sending birthday wish to admin: {}", name);
                System.out.println("✅ Sending admin birthday wish to: " + name);

                String msg =
                        "🎉 Happy Birthday, " + name + "! 🎂\n\n" +
                        "On this special day, we celebrate **you** — not just as an incredible partner, but as a visionary leader and an inspiring force behind FG Infotech's journey. 🚀\n\n" +
                        "May this new chapter bring you greater heights of success, endless happiness, and moments you’ll cherish forever.\n\n" +
                        "**Let’s keep building dreams together.** 💼✨\n\n" +
                        "With warmest wishes,\n" +
                        "— Your FG Infotech Family 💙\n" +
                        "🌐 https://fginfotech.in";

                emailService.sendAdminBirthdayMail(
                        admin.getEmail(),
                        "🎉 Happy Birthday to Our Rockstar Partner!",
                        msg
                );

                admin.setLastBirthdayWishedAt(today);
                adminRepository.save(admin);

                logger.info("✅ Birthday wish sent and saved for admin: {}", admin.getEmail());
            } else {
                logger.info("⏩ Skipping admin (not birthday or already wished this year): {}", admin.getLoginId());
            }
        }
    }
}
