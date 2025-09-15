package com.shri.main.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtil {

    private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Async("taskExecutor")
    public void sendEmailWithAttachment(String toEmail, String subject, String body, byte[] pdfBytes, String fileName) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body);
            helper.setFrom("future.growing.infotech@gmail.com");

            ByteArrayResource resource = new ByteArrayResource(pdfBytes);
            helper.addAttachment(fileName, resource);

            javaMailSender.send(message);

            System.out.println("✅ Email sent successfully to: " + toEmail);
            logger.info("✅ Email with attachment sent successfully to: {}", toEmail);
        } catch (Exception e) {
            System.err.println("❌ Failed to send email to: " + toEmail);
            logger.error("❌ Failed to send email to: {}", toEmail, e);
            e.printStackTrace();
        }
    }

    @Async("taskExecutor")
    public void sendEmailWithAttachmentManuallyCertSend(String toEmail, String subject, String body, byte[] pdfBytes, String fileName) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body);
            helper.setFrom("future.growing.infotech@gmail.com");

            ByteArrayResource resource = new ByteArrayResource(pdfBytes);
            helper.addAttachment(fileName, resource);

            javaMailSender.send(message);

            System.out.println("✅ Email sent successfully to: " + toEmail);
            logger.info("✅ Manually sent certificate email to: {}", toEmail);
        } catch (Exception e) {
            System.err.println("❌ Failed to send email to: " + toEmail);
            logger.error("❌ Failed to send manual certificate email to: {}", toEmail, e);
            e.printStackTrace();
        }
    }

    @Async("taskExecutor")
    public void sendFeesReceiptManuallyEmailWithAttachment(String toEmail, String subject, String body, byte[] pdfBytes, String fileName) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body);
            helper.setFrom("future.growing.infotech@gmail.com");

            ByteArrayResource resource = new ByteArrayResource(pdfBytes);
            helper.addAttachment(fileName, resource);

            javaMailSender.send(message);

            System.out.println("✅ Manually Fees Receipt Email sent successfully to: " + toEmail);
            logger.info("✅ Manually sent fees receipt email to: {}", toEmail);
        } catch (Exception e) {
            System.err.println("❌ Failed to send email to: " + toEmail);
            logger.error("❌ Failed to send fees receipt email to: {}", toEmail, e);
            e.printStackTrace();
        }
    }
}
