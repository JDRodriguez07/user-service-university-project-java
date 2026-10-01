package edu.university.user_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserFullResponseDTO {

    private Long id;

    // User
    private String email;
    private String status;
    private String role; // <--- AQUÍ YA ESTABA

    // Person
    private String documentType;
    private String dni;
    private String name;
    private String lastName;
    private String gender;
    private LocalDate birthDate;
    private String phoneNumber;
    private String address;

    // Admin
    private String adminCode;
    private String department;
    private String position;

    // Teacher
    private String teacherCode;
    private String specialization;
    private String academicDegree;
    private String contractType;
    private LocalDate hireDate;

    // Student
    private String studentCode;
    private LocalDate admissionDate;
    private LocalDate graduationDate;
    private BigDecimal gpa;
    private String career;
    private String studentStatus;
}
