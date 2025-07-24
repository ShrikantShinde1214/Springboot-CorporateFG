package com.shri.main.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shri.main.service.FeeSchedulerService;

@RestController
@RequestMapping("/test")
public class FeeTestController {

    private static final Logger logger = LoggerFactory.getLogger(FeeTestController.class);

    @Autowired
    private FeeSchedulerService schedulerService;

    @GetMapping("/send-weekly-fee-mails")
    public String triggerWeeklyEmails() {
        logger.info("Manual trigger for sending weekly fee emails started.");

        try {
            schedulerService.sendWeeklyFeeDetails(); 
            logger.info("Weekly fee emails sent successfully.");
            return "Weekly fee emails sent successfully!";
        } catch (Exception e) {
            logger.error("Error occurred while sending weekly fee emails", e);
            return "Failed to send weekly fee emails. Check logs for details.";
        }
    }
}
