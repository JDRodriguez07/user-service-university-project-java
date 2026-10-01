package edu.university.user_service.dto;

import edu.university.user_service.enums.AcademicDegree;
import edu.university.user_service.enums.ContractType;
import edu.university.user_service.enums.UserStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO used to expose Teacher information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherResponseDTO {

    private Long id;
    private String teacherCode;

    private String dni;
    private String name;
    private String lastName;
    private String phoneNumber;
    private String address;

    private String email;
    private UserStatus status;
    private String role;

    private String specialization;
    private AcademicDegree academicDegree;
    private ContractType contractType;
    private LocalDate hireDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
