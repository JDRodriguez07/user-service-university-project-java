package edu.university.user_service.dto;

import edu.university.user_service.enums.DocumentType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonUpdateDTO {

    private DocumentType documentType;
    private String dni;
    private String name;
    private String lastName;
    private String gender;
    private LocalDate birthDate;
    private String phoneNumber;
    private String address;
}
