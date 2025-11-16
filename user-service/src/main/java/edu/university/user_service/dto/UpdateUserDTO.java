package edu.university.user_service.dto;

import edu.university.user_service.enums.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO used to update an existing User.
 * Every field is optional.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDTO {

    @Email(message = "Invalid email format.")
    private String email;

    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters.")
    private String password;

    private UserStatus status;

    /**
     * Optional role change: ADMIN, TEACHER, STUDENT, etc.
     */
    private String role;
}
