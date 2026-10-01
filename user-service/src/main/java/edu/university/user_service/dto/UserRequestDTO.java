package edu.university.user_service.dto;

import edu.university.user_service.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO used to create or update a User.
 * For creation, email and password are required.
 * Role is required for creation (ADMIN, TEACHER, STUDENT).
 * Status is optional (defaults to ACTIVE when null).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    private String email;
    private String password;
    private UserStatus status; // optional
    private String role;       // ADMIN, TEACHER, STUDENT (optional on update)
    
}
