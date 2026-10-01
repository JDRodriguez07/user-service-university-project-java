package edu.university.user_service.exceptions;

public class PersonAlreadyExistsException extends RuntimeException {

    public PersonAlreadyExistsException(String documentType, String dni) {
        super("A person with documentType " + documentType + " and DNI " + dni + " already exists.");
    }
}
