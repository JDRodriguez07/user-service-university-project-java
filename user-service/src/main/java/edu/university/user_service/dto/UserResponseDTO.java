package edu.university.user_service.dto;

import edu.university.user_service.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO used to expose User information to clients,
 * hiding sensitive fields like the raw password.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String email;
    private UserStatus status;
    private String role;

}
