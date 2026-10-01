package edu.university.user_service.dto;

import edu.university.user_service.enums.StudentStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO used to create a new Student.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudentDTO {

    @NotBlank(message = "DNI is required.")
    private String dni;

    @NotBlank(message = "Name is required.")
    private String name;

    @NotBlank(message = "Last name is required.")
    private String lastName;

    private String phoneNumber;
    private String address;

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters.")
    private String password;

    @NotBlank(message = "Career is required.")
    private String career;

    private LocalDate admissionDate;
    private BigDecimal gpa;
    private StudentStatus studentStatus;
}
