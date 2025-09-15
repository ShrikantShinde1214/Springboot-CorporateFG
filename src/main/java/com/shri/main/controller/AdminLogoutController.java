package com.shri.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.shri.main.dao.AdminRepository;
import com.shri.main.model.Admin;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminLogoutController {

	@Autowired
	private AdminRepository adminRepo;
	@GetMapping("/logout")
	public String logout(HttpSession session) {
	    Admin sessionUser = (Admin) session.getAttribute("user");

	    if (sessionUser != null) {
	        Admin dbUser = adminRepo.findById(sessionUser.getId()).orElse(null);
	        if (dbUser != null) {
	            dbUser.setIsLogin(false);
	            adminRepo.save(dbUser);
	        }
	        session.invalidate();
	    }

	    return "redirect:/?loggedOut=true"; 
	}


}
