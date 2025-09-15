package com.shri.main.service;

import com.shri.main.dao.StudentDao;
import com.shri.main.model.Student;
import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

@Service
public class FeeSchedulerService {
	private static final Logger logger = LoggerFactory.getLogger(FeeSchedulerService.class);

    @Autowired
    private StudentDao studentRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;
    @Autowired
    CertificateGenerator certificateGenerator;
 
    @PostConstruct
    public void sendOnceOnStartup() throws Exception {
        sendWeeklyFeeReminderMails();
    }
  
    //send sendWelcomeAndCertificateMails
    @Scheduled(cron = "0 * * * * *", zone = "Asia/Kolkata") // Runs every 1 minute
    public void sendWelcomeAndCertificateMails() throws Exception {
        logger.info("Scheduler started at: ", java.time.LocalDateTime.now());

        List<Student> students = studentRepo.findAll();
        logger.info("Total students fetched: ", students.size());

        for (Student student : students) {
            if (student.getId() != 0 && !Boolean.TRUE.equals(student.getWelcomeMailSent())) {
                logger.info("Processing student: {} | Batch: {}", student.getName(), student.getBatchNo());

                try {
                    checkAndSendWelcomeEmailIfNew(student);
                    logger.info("Welcome email sent to: {}", student.getEmail());

                    sendCourseCompletionCertificate(student);
                    logger.info("Course completion certificate sent to: {}", student.getEmail());
                } catch (Exception e) {
                    logger.error("Error while processing student ID {}: {}", student.getId(), e.getMessage(), e);
                }
            }
        }

        logger.info("Scheduler finished at:", java.time.LocalDateTime.now());
    }


//send Scholarship,Placement,FullFeesMails
    @Scheduled(cron = "0 * * * * *", zone = "Asia/Kolkata") //run every 1 minute
    public void sendScholarshipPlacementAndFullFeesMails() throws Exception {
        List<Student> students = studentRepo.findAll();
        float epsilon = 0.01f;

        for (Student student : students) {

        	if (student.getPaidFees() == 1 && !Boolean.TRUE.equals(student.getScolarshipFullFeesMailSent())) {
        	    try {
        	        logger.info("Sending scholarship welcome email to: {} | Email: {}", student.getName(), student.getEmail());

        	        sendScholarshipWelcomeEmail(student);
        	        student.setScolarshipFullFeesMailSent(true);
        	        studentRepo.save(student);

        	        logger.info("Scholarship email sent successfully to: {}", student.getEmail());
        	    } catch (Exception e) {
        	        logger.error("Error sending scholarship email to {}: {}", student.getEmail(), e.getMessage(), e);
        	    }
        	}

        	if (Boolean.TRUE.equals(student.getPlaced()) && !Boolean.TRUE.equals(student.getPlacementMailSent())) {
        	    try {
        	        logger.info("Sending placement email to: {} | Email: {}", student.getName(), student.getEmail());

        	        sendPlacementEmail(student);
        	        student.setPlacementMailSent(true);
        	        studentRepo.save(student);

        	        logger.info("Placement email sent successfully to: {}", student.getEmail());
        	    } catch (Exception e) {
        	        logger.error("Error sending placement email to {}: {}", student.getEmail(), e.getMessage(), e);
        	    }
        	}


            boolean isFullyPaid = Math.abs(student.getTotalFees() - 45000.0f) < epsilon &&
                                  Math.abs(student.getPaidFees() - 45000.0f) < epsilon;

            if (isFullyPaid && !Boolean.TRUE.equals(student.getFullFeesMailSent())) {
               String subject = "FG Infotech - Fees Fully Paid & Receipt Attached";
                String htmlBody = """
                      <html><body>
                      <p>Welcome, <strong>%s</strong>,</p>
                      <p style="color:green;">
                      Congratulations! 🎉<br>
                      Your total fees have been successfully paid.<br>
                      Please find your official fee receipt attached in PDF format.<br><br>
                      For queries, call us at <strong>9511200293</strong>.
                      </p>
                      <br><br>
                      Thanks & Regards,<br>
                      <strong>FG Infotech</strong><br>
                      Website: <a href='www.fginfotech.in'>fginfotech.in</a>
                      </body></html>
                      """.formatted(student.getName());

              byte[] pdf = pdfGeneratorService.generateFeesReceiptPdf(student);

              

                emailService.sendEmailWithAttachment(
                    student.getEmail(), subject, htmlBody,
                    "FeesReceipt_" + student.getName() + ".pdf", pdf);

                student.setFullFeesMailSent(true);
                studentRepo.save(student);
            }
        }
    }

    @Scheduled(cron = "0 0 19 ? * MON", zone = "Asia/Kolkata") // Every Monday at 7:00 PM IST
    public void sendWeeklyFeeReminderMails() {
        logger.info("Weekly fee reminder scheduler started at {}", java.time.LocalDateTime.now());

        List<Student> students = studentRepo.findAll();
        logger.info("Total students fetched: {}", students.size());

        int sentCount = 0;
        int skippedCount = 0;

        for (Student student : students) {
            if (student.getPaidFees() >= student.getTotalFees()) {
                logger.info("Skipping (Fully Paid): {} | Email: {}", student.getName(), student.getEmail());
                skippedCount++;
                continue;
            }

            if (student.getPaidFees() == 1) {
                logger.info("Skipping (Paid only 1): {} | Email: {}", student.getName(), student.getEmail());
                skippedCount++;
                continue;
            }

            if (student.getReminderEmailCount() >= 25) {
                logger.info("Skipping (25 reminders already sent): {} | Email: {}", student.getName(), student.getEmail());
                skippedCount++;
                continue;
            }

            try {
                String subject = "FG Infotech Fees Details:";
                String body = generateHtmlBody(student);

                emailService.sendEmail(student.getEmail(), subject, body);
                student.setReminderEmailCount(student.getReminderEmailCount() + 1);
                studentRepo.save(student);

                logger.info("Reminder email sent to: {} | Count: {}", student.getEmail(), student.getReminderEmailCount());
                sentCount++;
            } catch (Exception e) {
                logger.error("Error sending email to {}: {}", student.getEmail(), e.getMessage(), e);
            }
        }

        logger.info("Weekly reminder summary - Sent: {}, Skipped: {}", sentCount, skippedCount);
        logger.info("Weekly fee reminder scheduler finished at {}", java.time.LocalDateTime.now());
    }

    private void checkAndSendWelcomeEmailIfNew(Student student) {
        if (student.getId() != 0 && !Boolean.TRUE.equals(student.getWelcomeMailSent())) {
            try {
                logger.info("Sending welcome email to: {} | Email: {}", student.getName(), student.getEmail());

                sendWelcomeEmail(student);
                student.setWelcomeMailSent(true);
                studentRepo.save(student);

                logger.info("Welcome email sent and updated in DB: {}", student.getEmail());
            } catch (Exception e) {
                logger.error("Error sending welcome email to {}: {}", student.getEmail(), e.getMessage(), e);
            }
        } else {
            logger.error("Skipping welcome email for: {} | Already sent or invalid ID", student.getEmail());
        }
    }

    private void sendWelcomeEmail(Student student) {
        String subject = "Welcome to FG Infotech - Your Journey Begins!";
        String htmlBody = """
                <html>
                <body style="font-family: Arial, sans-serif;">
                    <div style="border:1px solid #ddd; border-radius:10px; padding:20px; max-width:600px; margin:auto;">
                        <h2 style="color:green;">Welcome to FG Infotech, %s!</h2>
                        <p>We're excited to have you join us. Here are your admission details:</p>

                        <table border="1" cellpadding="10" cellspacing="0" style="border-collapse:collapse; width:100%%;">
                            <tr><th align="left">Full Name</th><td>%s</td></tr>
                            <tr><th align="left">Batch No</th><td>%s</td></tr>
                            <tr><th align="left">Date of Admission</th><td>%s</td></tr>
                        </table>

                        <br>
                        <p>If you have any questions, contact us at <strong>9511200293</strong>.</p>

                        <p>
                            Thanks & Regards,<br>
                            <strong>FG Infotech</strong><br>
                            <a href='www.fginfotech.in'>fginfotech.in</a><br><br>
                        </p>
                    </div>
                </body>
                </html>
                """.formatted(
                student.getName(),
                student.getName(),
                student.getBatchNo(),
                new SimpleDateFormat("dd MMM yyyy").format(student.getAdmissionDate())
        );

        emailService.sendEmail(student.getEmail(), subject, htmlBody);
    }

    private void sendScholarshipWelcomeEmail(Student student) {
        String subject = "🎓 Welcome to FG Infotech - Scholarship Confirmation!";

        String htmlBody = """
            <html>
            <body style="font-family: Arial, sans-serif;">
                <div style="border:1px solid #ddd; border-radius:10px; padding:20px; max-width:600px; margin:auto;">
                    <h2 style="color:#1e88e5;">Hello %s, Welcome to FG Infotech!</h2>

                    <p>Congratulations! You have been granted a full scholarship at FG Infotech. 🎉</p>

                    <p>Here are your enrollment details:</p>

                    <table border="1" cellpadding="10" cellspacing="0" style="border-collapse:collapse; width:100%%;">
                        <tr><th align="left">Full Name</th><td>%s</td></tr>
                        <tr><th align="left">Batch No</th><td>%s</td></tr>
                        <tr><th align="left">Date of Admission</th><td>%s</td></tr>
                        <tr><th align="left">Total Fees</th><td><b>₹%d</b></td></tr>
                        <tr><th align="left">Scholarship Received</th><td><b>Full Scholarship (₹%d)</b></td></tr>
                        <tr><th align="left">Pending Fees</th><td><b>₹0</b></td></tr>
                    </table>

                    <br>
                    <p>If you have any questions, feel free to reach us at <strong>9511200293</strong>.</p>

                    <p>
                        Thanks & Regards,<br>
                        <strong>FG Infotech</strong><br>
                        <a href='www.fginfotech.in'>www.fginfotech.in</a>
                    </p>
                </div>
            </body>
            </html>
            """.formatted(
            student.getName(),
            student.getName(),
            student.getBatchNo(),
            new SimpleDateFormat("dd MMM yyyy").format(student.getAdmissionDate()),
            (int) student.getTotalFees(),
            (int) student.getTotalFees()
        );

        emailService.sendEmail(student.getEmail(), subject, htmlBody);
    }

    private void sendPlacementEmail(Student student) {
        String subject = "🎉 Congratulations on Your Placement - FG Infotech";
        String htmlBody = """
            <html>
            <body style="font-family: Arial, sans-serif;">
                <div style="border:1px solid #ddd; border-radius:10px; padding:20px; max-width:600px; margin:auto;">
                    <h2 style="color:green;">Congratulations %s!</h2>
                    <p>We are thrilled to inform you that you have been successfully placed.</p>

                    <table border="1" cellpadding="10" cellspacing="0" style="border-collapse:collapse; width:100%%;">
                        <tr><th align="left">Full Name</th><td>%s</td></tr>
                        <tr><th align="left">Batch No</th><td>%s</td></tr>
                        <tr><th align="left">Date of Admission</th><td>%s</td></tr>
                        <tr><th align="left">Pending Fees</th><td style="color:red;">₹%.2f</td></tr>
                        <tr><th align="left">Status</th><td style="color:green;"><b>Placed ✅</b></td></tr>
                    </table>

                    <br>
                    <p>If you have any questions, feel free to contact us at <strong>9511200293</strong>.</p>

                    <p>
                        Thanks & Regards,<br>
                        <strong>FG Infotech Placement Team</strong><br>
                        <a href='www.fginfotech.in'>fginfotech.in</a>
                    </p>
                </div>
            </body>
            </html>
            """.formatted(
            student.getName(), 
            student.getName(), 
            student.getBatchNo(), 
            new SimpleDateFormat("dd MMM yyyy").format(student.getAdmissionDate()), // Date
            (student.getTotalFees() - student.getPaidFees())
        );

        emailService.sendEmail(student.getEmail(), subject, htmlBody);
    }    
    public void sendCourseCompletionCertificate(Student student) throws Exception {
       if (student.getId() != 0 && Boolean.TRUE.equals(student.getCertificateSent())) {

          logger.info("sendCourseCompletionCertificate Certificate already sent to: " + student.getEmail());
            return;
        }

     LocalDate admissionDate = student.getAdmissionDate().toLocalDate();

       LocalDate currentDate = LocalDate.now();
       LocalDate eligibleDate = admissionDate.plusMonths(6);

        if (currentDate.isBefore(eligibleDate)) {
            System.out.println("⚠️ Certificate not eligible yet. Will be eligible after: " + eligibleDate);
            return;
        }
       byte[] certificateBytes = certificateGenerator.generateCertificate(student);

        String subject = "🎓 Course Completion Certificate - FG Infotech";
      
        String body = String.format("""
                Dear %s,

                Congratulations on successfully completing your course at FG Infotech!

                Attached is your official Course Completion Certificate.

                Thanks & Regards,
                FG Infotech Team.
                Website: www.fginfotech.in
                """, student.getName());

      emailService.sendEmailWithAttachmentBytes(
                student.getEmail(),
                subject,
                body,
                certificateBytes,
                "Course_Completion_Certificate_" + student.getId() + ".pdf"
        );

        student.setCertificateSent(true);
        studentRepo.save(student);
    }

    private String generateHtmlBody(Student student) {
        return """
                <html>
                <body>
                   <p>Welcome, <strong>%s</strong>,</p>
                   <p>
                    Please find your fee details below and kindly pay your pending fees.
                    For further information, feel free to contact us at <strong>9511200293</strong>.
                   </p>
                   <table border='1' cellpadding='10' cellspacing='0' style='border-collapse: collapse;'>
                       <tr>
                           <th>Total Fees</th>
                           <th>Received Fees</th>
                           <th>Pending Fees</th>
                       </tr>
                       <tr>
                           <td>%.2f</td>
                           <td>%.2f</td>
                           <td>%.2f</td>
                       </tr>
                   </table>
                   <br><br>
                   Thanks & Regards,<br>
                   <strong>FG Infotech</strong><br>
                   <a href='www.fginfotech.in'>fginfotech.in</a>
                </body>
                </html>
                """.formatted(student.getName(),
                (double) student.getTotalFees(),
                (double) student.getPaidFees(),
                (double) (student.getTotalFees() - student.getPaidFees()));
    }
   
}
