package com.shri.main.model;

import java.sql.Date;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Table(name = "tbl_students")
@Data
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	@Column(name = "name")
	private String name;
	@Column(name = "email")
	private String email;
	@Column(name = "mobile")
	private String mobile;
	@Column(name = "batchNo")
	private String batchNo;
	@Temporal(TemporalType.DATE)
    @Column(name = "admissionDate")
	private Date admissionDate;
	@Temporal(TemporalType.DATE)
	@Column(name = "birthDate",nullable = true)
	private Date birthDate;
    @Column(name = "totalFees")
	private float totalFees=45000F;
	@Column(name = "paidFees")
	private float paidFees;
	@Column(name = "createdBy")
	private String createdBy;
	@Column(name = "modifiedBy")
	private String modifiedBy;
	@Column(name = "course")
	private String course;
	@Column(name = "comments")
	private String comments;
	@Column(name = "highestQualification")
	private String highestQualification;
	
	//Timestamp
	@CreationTimestamp
	@Column(name = "createdOn", nullable = false, updatable = false)
	private String createdOn;
	@UpdateTimestamp
	@Column(name = "updatedOn")
	private String updatedOn;
	
	private Boolean welcomeMailSent = false;
	private Boolean welcomeMessageSent = false;

    @Column(name = "full_fees_mail_sent")
    private Boolean fullFeesMailSent = false;

    //scolarship candiate mail sent 1 time START HERE
    @Column(name = "scolarship_fees_mail_sent")
    private Boolean scolarshipFullFeesMailSent = false;
    //scolarship candiate mail sent 1 time END HERE
    
    //for identification flag for placed students
    @Column(name = "placed")
    private Boolean placed = false;
    
    //count of fess reminder mail for every student 
    @Column(name = "reminder_email_count")
    private int reminderEmailCount;
    
    //placement student mail send  START HERE
    @Column(name = "placement_mail_sent")
    private Boolean placementMailSent;
    
  //placement student mail send  END HERE
    @Column(name = "is_certificate_sent")
    private Boolean certificateSent;
 
    //Student birthday wish send
    @Column(name = "isBirthdayWishSent")
    private Boolean isBirthdayWishSent;


}
