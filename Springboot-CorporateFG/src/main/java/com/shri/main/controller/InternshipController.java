package com.shri.main.controller;

import com.shri.main.model.Internship;
import com.shri.main.service.impl.InternshipService;
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
public class InternshipController {

    private static final Logger logger = LoggerFactory.getLogger(InternshipController.class);

    @Autowired
    private InternshipService internshipService;

    @GetMapping("/sendInternship")
    public String showForm(Model model) {
        logger.info("GET /sendInternship - Displaying internship certificate form.");
        model.addAttribute("internship", new Internship());
        return "internship-cert-send";
    }

    @PostMapping("/sendInternship")
    public String submitForm(@ModelAttribute("internship") Internship internship,
                             RedirectAttributes redirectAttributes) {
        logger.info("POST /sendInternship - Received internship certificate form for: {}", internship.getName());

        try {
            internshipService.processInternship(internship);
            logger.info("Internship certificate sent successfully to: {}", internship.getEmail());
            redirectAttributes.addFlashAttribute("message", "Internship certificate sent successfully!");
        } catch (Exception e) {
            logger.error("Error while sending internship certificate for: {}", internship.getEmail(), e);
            redirectAttributes.addFlashAttribute("error", "Failed to send internship certificate. Please try again.");
        }

        return "redirect:/sendInternship";
    }
}
