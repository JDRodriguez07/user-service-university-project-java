package edu.university.user_service.dto;

import edu.university.user_service.enums.UserStatus;
import lombok.*;

/**
 * DTO used to expose Administrator information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdministratorResponseDTO {

    private Long id;
    private String adminCode;

    private String dni;
    private String name;
    private String lastName;
    private String phoneNumber;
    private String address;

    private String email;
    private UserStatus status;
    private String role;

    private String department;
    private String position;

}
