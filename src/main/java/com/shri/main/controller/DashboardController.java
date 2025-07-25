package com.shri.main.controller;

import com.shri.main.dao.StudentDashboardDao;
import com.shri.main.model.Student;
import com.shri.main.service.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private StudentDashboardDao studentDashboardDao;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        logger.info("GET /dashboard - Loading dashboard statistics");
        Map<String, Integer> stats = dashboardService.getDashboardStats();
        model.addAllAttributes(stats);
        logger.info("Dashboard data: {}", stats);
        return "dashboard";
    }

    @PostMapping("/placeStudent")
    public String placeStudent(@RequestParam String name, RedirectAttributes redirectAttributes) {
        logger.info("POST /placeStudent - Marking placement for student: {}", name);

        try {
            Student s = studentDashboardDao.getByName(name);
            //if (s != null && !s.isPlaced()) {
            	   if (s != null && !Boolean.TRUE.equals(s.getPlaced())) {
                s.setPlaced(true);
                studentDashboardDao.save(s);
                logger.info("marked as placed", s.getName());
                redirectAttributes.addFlashAttribute("placementSuccess", "✅ " + s.getName() + " marked as placed!");
            } else if (s != null) {
                logger.warn("{} is already placed", s.getName());
                redirectAttributes.addFlashAttribute("placementError", "⚠️ " + s.getName() + " is already placed.");
            } else {
                logger.warn("Student not found with name: {}", name);
                redirectAttributes.addFlashAttribute("placementError", "❌ Student not found.");
            }
        } catch (Exception e) {
            logger.error("Error while placing student: {}", name, e);
            redirectAttributes.addFlashAttribute("placementError", "❌ An error occurred.");
        }

        return "redirect:/list";
    }

    @GetMapping("/placed-data")
    @ResponseBody
    public List<Student> getPlacedStudents() {
        logger.info("GET /placed-data - Fetching all placed students");
        List<Student> placedStudents = studentDashboardDao.findByPlacedTrue();
        logger.info("Found {} placed students", placedStudents.size());
        return placedStudents;
    }
}
