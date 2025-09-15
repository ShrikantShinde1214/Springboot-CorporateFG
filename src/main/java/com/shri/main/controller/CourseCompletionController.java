package com.shri.main.controller;

import com.shri.main.model.CourseCertManuallySend;
import com.shri.main.service.impl.CourseCompletionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CourseCompletionController {

    private static final Logger logger = LoggerFactory.getLogger(CourseCompletionController.class);

    @Autowired
    private CourseCompletionServiceImpl completionService;

    @GetMapping("/sendCourseCompletion")
    public String showForm(Model model) {
        logger.info("GET /sendCourseCompletion - Displaying manual course completion certificate form.");
        model.addAttribute("courseCompletion", new CourseCertManuallySend());
        return "course-completion-send";
    }

    @PostMapping("/sendCourseCompletion")
    public String submitForm(@ModelAttribute("courseCompletion") CourseCertManuallySend courseCompletion,
                             RedirectAttributes redirectAttributes) {
        logger.info("POST /sendCourseCompletion - Request to manually send certificate for: {}", courseCompletion.getName());

        try {
            completionService.processCourseCompletion(courseCompletion);
            logger.info("✅ Course completion certificate sent manually to: {}", courseCompletion.getEmail());
            redirectAttributes.addFlashAttribute("message", "Course Completion Certificate sent manually successfully!");
        } catch (Exception e) {
            logger.error("❌ Failed to send course completion certificate to: {}", courseCompletion.getEmail(), e);
            redirectAttributes.addFlashAttribute("error", "Failed to send course completion certificate.");
        }

        return "redirect:/sendCourseCompletion";
    }
}
