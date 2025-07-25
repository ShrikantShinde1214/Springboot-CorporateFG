package com.shri.main.service;


import com.shri.main.dao.AdminDashboardDao;
import com.shri.main.dao.StudentDashboardDao;
import com.shri.main.model.Student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

	@Autowired
    private StudentDashboardDao studentDashboardDao;

    @Autowired
    private AdminDashboardDao adminDashboardDao;
    
    //PLACE student mark
    public boolean placeStudent(Student s) {
	    int updated = studentDashboardDao.markStudentAsPlaced(s.getName()); 
	    return updated > 0;
	}
	public Student getByName(String name) {
	    return studentDashboardDao.findByName(name);
	}
	 //PLACE student mark

    public Map<String, Integer> getDashboardStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalStudents", studentDashboardDao.getTotalStudents());
        stats.put("paidStudents", studentDashboardDao.getPaidStudents());
        stats.put("placedStudents", studentDashboardDao.getPlacedStudents());
        stats.put("allCourses", studentDashboardDao.getAllCourses());
        stats.put("allTFeesNill", studentDashboardDao.getAllTFeesNill());
        stats.put("scholarships", studentDashboardDao.getReceivedFees());
        stats.put("totalAdmins", adminDashboardDao.getTotalAdmins());
        stats.put("allBatches", studentDashboardDao.getAllBatches());
        stats.put("totalFees", (int) studentDashboardDao.getTotalFees().floatValue());         
        stats.put("totalpaidFeesSum", (int) studentDashboardDao.getReceivedFees11().floatValue());
        stats.put("pendingFees", studentDashboardDao.getPendingFees());     
        stats.put("scholarships1", studentDashboardDao.getScholarships()); 
        return stats;
    }
}
