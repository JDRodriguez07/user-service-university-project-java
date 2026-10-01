package edu.university.user_service.model;

import edu.university.user_service.enums.AcademicDegree;
import edu.university.user_service.enums.ContractType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teachers")
public class Teacher extends Person {

    @Column(name = "teacher_code", nullable = false, length = 30)
    private String teacherCode;

    @Column(name = "specialization", length = 120)
    private String specialization;

    @Enumerated(EnumType.STRING)
    @Column(name = "academic_degree", length = 20, nullable = false)
    private AcademicDegree academicDegree = AcademicDegree.BACHELOR;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_type", length = 20, nullable = false)
    private ContractType contractType = ContractType.FULL_TIME;

    @Column(name = "hire_date")
    private LocalDate hireDate;
}
