package com.shri.main.listener;

import com.shri.main.model.Admin;
import com.shri.main.dao.AdminRepository;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SessionTimeoutListener implements HttpSessionListener {

    private static final Logger logger = LoggerFactory.getLogger(SessionTimeoutListener.class);

    @Autowired
    private AdminRepository adminRepo;

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        logger.info("Session destroyed. Checking for logged-in user to update login status.");

        Admin admin = (Admin) se.getSession().getAttribute("user");
        if (admin != null) {
            admin.setIsLogin(false);
            adminRepo.save(admin);
            logger.info("Session expired → Login flag set to false for admin: {}", admin.getUsername());
        } else {
            logger.warn("No admin found in session attributes during session destruction.");
        }
    }
}
