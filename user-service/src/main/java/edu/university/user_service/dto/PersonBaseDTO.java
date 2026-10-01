package edu.university.user_service.dto;

import edu.university.user_service.enums.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonBaseDTO {

    @NotNull(message = "Document type is required.")
    private DocumentType documentType;

    @NotBlank(message = "DNI is required.")
    private String dni;

    @NotBlank(message = "Name is required.")
    private String name;

    @NotBlank(message = "Last name is required.")
    private String lastName;

    @NotBlank(message = "Gender is required.")
    private String gender;

    @NotNull(message = "Birth date is required.")
    private LocalDate birthDate;

    @NotBlank(message = "Phone number is required.")
    private String phoneNumber;

    @NotBlank(message = "Address is required.")
    private String address;
}
