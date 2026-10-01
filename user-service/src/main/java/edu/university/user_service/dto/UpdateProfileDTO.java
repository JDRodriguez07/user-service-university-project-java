package edu.university.user_service.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para que el propio usuario actualice algunos datos de su perfil.
 * Todos los campos son opcionales (parcial).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileDTO {

    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters.")
    private String password;

    // Datos de Person
    private String gender;
    private String phoneNumber;
    private String address;
}
