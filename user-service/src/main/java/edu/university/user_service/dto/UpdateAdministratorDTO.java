package edu.university.user_service.dto;

import edu.university.user_service.enums.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO used to update an existing Administrator.
 * All fields are optional; only non-null values are applied.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAdministratorDTO {

    private String dni;
    private String name;
    private String lastName;
    private String phoneNumber;
    private String address;

    @Email(message = "Invalid email format.")
    private String email;

    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters.")
    private String password;

    private UserStatus status;

    private String department;
    private String position;
}
