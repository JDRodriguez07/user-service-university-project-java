package edu.university.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO used to create a new User.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters.")
    private String password;

    @NotBlank(message = "Role is required. Example: ADMIN, STUDENT, TEACHER")
    private String role;
}
