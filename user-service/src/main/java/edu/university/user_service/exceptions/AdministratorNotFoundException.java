package edu.university.user_service.exceptions;

/**
 * Exception thrown when an Administrator cannot be found by its ID.
 */
public class AdministratorNotFoundException extends RuntimeException {

    /**
     * Constructor for AdministratorNotFoundException.
     *
     * @param id the ID of the administrator that was not found
     */
    public AdministratorNotFoundException(Long id) {
        super("Administrator with ID " + id + " was not found.");
    }
}
