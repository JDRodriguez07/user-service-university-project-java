package edu.university.user_service.exceptions;

public class DocumentTypeAndDniAlreadyExistsException extends RuntimeException {
    
    public DocumentTypeAndDniAlreadyExistsException() {
        super("Document type and DNI already exists");
    }
}
