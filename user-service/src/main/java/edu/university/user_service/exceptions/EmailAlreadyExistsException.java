package edu.university.user_service.exceptions;

/**
 * Exception thrown when attempting to register or update a user
 * with an email address that already exists in the system.
 */
public class EmailAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new EmailAlreadyExistsException with a message
     * including the duplicated email address.
     *
     * @param email the email address that is already registered
     */
    public EmailAlreadyExistsException(String email) {
        super("The email '" + email + "' is already registered.");
    }
}
