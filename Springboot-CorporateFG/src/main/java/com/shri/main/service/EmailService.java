package com.shri.main.service;


import jakarta.activation.DataSource;
import jakarta.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Async("taskExecutor") // async execution
    public void sendEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("future.growing.infotech@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Async("taskExecutor") // async execution
    public void sendEmailWithAttachment(String to, String subject, String htmlBody, String filename, byte[] pdf) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            helper.addAttachment(filename, new ByteArrayDataSource(pdf, "application/pdf"));

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Async("taskExecutor") // async execution
    public void sendEmailWithAttachmentBytes(String to, String subject, String body, byte[] pdfBytes, String fileName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("future.growing.infotech@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);

            DataSource dataSource = new ByteArrayDataSource(pdfBytes, "application/pdf");
            helper.addAttachment(fileName, dataSource);

            mailSender.send(message);
            System.out.println("✅ Certificate email sent to: " + to);
        } catch (Exception e) {
            System.out.println("❌ Failed to send certificate email to: " + to);
            e.printStackTrace();
        }
    }

    @Async("taskExecutor") // async execution
    public void sendOtpEmail(String toEmail, int otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your FG Infotech Login OTP");
        message.setText("Dear Admin,\n\n"
                + "Your OTP for login is: " + otp + "\n\n"
                + "Note:\n"
                + "- This OTP is valid for only **3 minutes**.\n"
                + "- Do not share this OTP with anyone.\n\n"
                + "Please enter it to complete your login.\n\n"
                + "Regards,\n"
                + "FG Infotech");
        mailSender.send(message);
    }

    //Birthday wish send
    @Async("taskExecutor") // async execution
    public void sendBirthdayWishMail(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("future.growing.infotech@gmail.com"); 
            mailSender.send(message);
            System.out.println("✅ Email sent to: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Failed to send email to: " + toEmail);
            e.printStackTrace();
        }
    }
//Admin birthday wish send mail
    @Async("taskExecutor") // async execution
    public void sendAdminBirthdayMail(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("future.growing.infotech@gmail.com");

            mailSender.send(message);
            System.out.println("✅ Email sent to: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Failed to send email to: " + toEmail);
            e.printStackTrace();
        }
    }   
    
}
