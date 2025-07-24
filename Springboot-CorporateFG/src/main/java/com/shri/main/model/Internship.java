package com.shri.main.model;

import java.time.LocalDate; // instead of java.util.Date

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Internship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String email;

    private LocalDate fromDate;
    private LocalDate toDate;

    private String role;

    private boolean certificateSent;

}
