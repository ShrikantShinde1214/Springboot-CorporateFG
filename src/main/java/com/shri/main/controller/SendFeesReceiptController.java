package com.shri.main.controller;

import java.util.List;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shri.main.dao.StudentDao;
import com.shri.main.model.Student;
import com.shri.main.service.FeesReceiptPdfService;
import com.shri.main.service.impl.StudentSearchService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/send-receipt")
public class SendFeesReceiptController {

    private static final Logger logger = LoggerFactory.getLogger(SendFeesReceiptController.class);

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private StudentSearchService studentSearchService;

    @Autowired
    private FeesReceiptPdfService feesReceiptPdfService;

    // Show the form with batch dropdown
    @GetMapping("/form")
    public String showForm(Model model) {
        logger.info("Displaying fees receipt form page.");
        model.addAttribute("batchList", studentSearchService.findAllBatchNos());
        return "sendFeesReceiptForm";
    }

    // Search students by name or batch
    @PostMapping("/search")
    public String search(@RequestParam(required = false) String name,
                         @RequestParam(required = false) String batch,
                         Model model) {
        logger.info("Searching students by name: {} or batch: {}", name, batch);
        List<Student> students = studentSearchService.findByNameOrBatch(name, batch);
        model.addAttribute("students", students);
        model.addAttribute("batchList", studentSearchService.findAllBatchNos());
        return "sendFeesReceiptForm";
    }

    // Send receipt to one student
    @PostMapping("/send")
    public String sendSingle(@RequestParam("studentId") int studentId, RedirectAttributes ra) {
        try {
            Student student = studentDao.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            logger.info("Sending fees receipt to student: {} (ID: {})", student.getName(), student.getId());
            feesReceiptPdfService.sendFeesReceiptPdf(student);

            ra.addFlashAttribute("success", "Receipt sent successfully to: " + student.getName());
            logger.info("Receipt sent successfully to: {}", student.getEmail());
        } catch (Exception e) {
            logger.error("Failed to send receipt to student ID: {}", studentId, e);
            ra.addFlashAttribute("error", "Failed to send receipt.");
        }
        return "redirect:/send-receipt/form";
    }

    // Send receipts to multiple selected students
    @PostMapping("/send-multiple")
    public String sendMultiple(@RequestParam("studentIds") List<Integer> ids, RedirectAttributes ra) {
        logger.info("Sending receipts to multiple students: {}", ids);
        int successCount = 0;
        int failCount = 0;

        for (int id : ids) {
            try {
                Student student = studentDao.findById(id)
                        .orElseThrow(() -> new RuntimeException("Student not found"));
                feesReceiptPdfService.sendFeesReceiptPdf(student);
                logger.info("✅ Receipt sent to: {}", student.getEmail());
                successCount++;
            } catch (Exception e) {
                logger.error("❌ Failed to send receipt to student ID: {}", id, e);
                failCount++;
            }
        }

        if (failCount == 0) {
            ra.addFlashAttribute("success", "Receipts sent to all selected students successfully.");
        } else {
            ra.addFlashAttribute("error", successCount + " receipts sent successfully. " +
                    failCount + " failed.");
        }

        return "redirect:/send-receipt/form";
    }

    // Download receipt as PDF
    @GetMapping("/download")
    public void generatePdfForStudentId(@RequestParam("studentId") int studentId, HttpServletResponse response) throws Exception {
        Student student = studentDao.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        logger.info("Downloading PDF receipt for student ID: {}", studentId);

        byte[] pdfBytes = feesReceiptPdfService.generateFeesReceiptManuallyPdf(student);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=FeesReceipt_" + student.getName().replaceAll(" ", "_") + ".pdf");
        response.setContentLength(pdfBytes.length);

        OutputStream out = response.getOutputStream();
        out.write(pdfBytes);
        out.flush();

        logger.info("Download complete for: {}", student.getEmail());
    }

    // Preview receipt as inline PDF
    @GetMapping("/preview")
    public void previewPdfForStudent(@RequestParam("studentId") int studentId, HttpServletResponse response) throws Exception {
        Student student = studentDao.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        logger.info("Previewing PDF receipt for student ID: {}", studentId);

        byte[] pdfBytes = feesReceiptPdfService.generateFeesReceiptManuallyPdf(student);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=FeesReceipt_" + student.getName().replaceAll(" ", "_") + ".pdf");
        response.setContentLength(pdfBytes.length);

        OutputStream out = response.getOutputStream();
        out.write(pdfBytes);
        out.flush();

        logger.info("Preview displayed for: {}", student.getEmail());
    }
}
