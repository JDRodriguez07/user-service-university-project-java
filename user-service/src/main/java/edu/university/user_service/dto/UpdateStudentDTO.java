package edu.university.user_service.dto;

import edu.university.user_service.enums.StudentStatus;
import edu.university.user_service.enums.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO used to update an existing Student.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStudentDTO {

    private String dni;
    private String name;
    private String lastName;
    private String phoneNumber;
    private String address;

    @Email(message = "Invalid email format.")
    private String email;

    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters.")
    private String password;

    private UserStatus status;

    private String career;
    private LocalDate admissionDate;
    private LocalDate graduationDate;
    private BigDecimal gpa;
    private StudentStatus studentStatus;
}
