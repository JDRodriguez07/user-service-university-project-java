package edu.university.user_service.model;

import edu.university.user_service.enums.StudentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "students")
public class Student extends Person {

    @Column(name = "student_code", nullable = false, length = 30)
    private String studentCode;

    @Column(name = "admission_date")
    private LocalDate admissionDate;

    @Column(name = "graduation_date")
    private LocalDate graduationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "student_status", nullable = false, length = 20)
    private StudentStatus studentStatus = StudentStatus.ENROLLED;

    @Column(name = "gpa", precision = 3, scale = 2) // ej. 4.50, 3.80
    private BigDecimal gpa;

    @Column(name = "career", length = 120)
    private String career;
}
