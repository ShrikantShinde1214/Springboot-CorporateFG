package com.shri.main.model;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "student_history")
@Data
public class StudentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;

    private float totalFees;

    private float paidFees;

    private String comments;

    @CreationTimestamp
    @Column(name = "createdOn", nullable = false, updatable = false)
    private String createdOn;

    @UpdateTimestamp
    @Column(name = "updatedOn")
    private String updatedOn;
   
}
