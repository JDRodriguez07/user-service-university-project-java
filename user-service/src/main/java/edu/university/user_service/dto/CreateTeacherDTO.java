package edu.university.user_service.dto;

import edu.university.user_service.enums.AcademicDegree;
import edu.university.user_service.enums.ContractType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateTeacherDTO extends PersonBaseDTO {

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters.")
    private String password;

    @NotBlank(message = "Specialization is required.")
    private String specialization;

    @NotNull(message = "Academic degree is required.")
    private AcademicDegree academicDegree;

    @NotNull(message = "Contract type is required.")
    private ContractType contractType;

    private LocalDate hireDate;
}
