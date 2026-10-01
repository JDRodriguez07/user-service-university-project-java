package edu.university.user_service.exceptions;

/**
 * Exception thrown when a User cannot be found by its identifier.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructs a new UserNotFoundException with a message
     * including the missing user ID.
     *
     * @param id the ID of the user that was not found
     */
    public UserNotFoundException(Long id) {
        super("User with ID " + id + " was not found.");
    }

    /**
     * Constructs a new UserNotFoundException with a message
     * including the missing user email.
     *
     * @param email the email of the user that was not found
     */
    public UserNotFoundException(String email) {
        super("User with email '" + email + "' was not found.");
    }
}
