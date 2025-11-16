package edu.university.user_service.dto;

import edu.university.user_service.enums.StudentStatus;
import edu.university.user_service.enums.UserStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO used to expose Student information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseDTO {

    private Long id;
    private String studentCode;

    private String dni;
    private String name;
    private String lastName;
    private String phoneNumber;
    private String address;

    private String email;
    private UserStatus status;
    private String role;

    private String career;
    private LocalDate admissionDate;
    private LocalDate graduationDate;
    private BigDecimal gpa;
    private StudentStatus studentStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
