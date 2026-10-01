package edu.university.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO used to create a new Administrator.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdministratorDTO {

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

    @NotBlank(message = "Department is required.")
    private String department;

    @NotBlank(message = "Position is required.")
    private String position;
}
