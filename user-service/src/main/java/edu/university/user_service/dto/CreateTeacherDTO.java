package edu.university.user_service.dto;

import edu.university.user_service.enums.AcademicDegree;
import edu.university.user_service.enums.ContractType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO used to create a new Teacher.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTeacherDTO {

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
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters.")
    private String password;

    private String specialization;

    private AcademicDegree academicDegree;

    private ContractType contractType;

    private LocalDate hireDate;
}
