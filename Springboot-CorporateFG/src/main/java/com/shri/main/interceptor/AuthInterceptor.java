package com.shri.main.interceptor;

import com.shri.main.model.Admin;
import com.shri.main.dao.AdminRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            logger.warn("Session expired or user not found in session.");

            // Try to fetch last known user from session (optional)
            Admin lastUser = (session != null) ? (Admin) session.getAttribute("lastUser") : null;

            if (lastUser != null) {
                Admin dbUser = adminRepository.findById(lastUser.getId()).orElse(null);
                if (dbUser != null) {
                    dbUser.setIsLogin(false);
                    adminRepository.save(dbUser);
                    logger.info("isLogin set to false for admin: {}", dbUser.getUsername());
                }
            }

            if (session != null) {
                session.invalidate();
            }

            logger.info("Redirecting to login page with sessionExpired=true");
            response.sendRedirect("/?sessionExpired=true");
            return false;
        }

        return true;
    }
}
