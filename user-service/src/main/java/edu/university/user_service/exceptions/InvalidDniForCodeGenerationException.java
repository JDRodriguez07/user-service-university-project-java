package edu.university.user_service.exceptions;

/**
 * Exception thrown when a DNI value is invalid or cannot be used
 * to generate a code for a person (admin, teacher, etc.).
 */
public class InvalidDniForCodeGenerationException extends RuntimeException {

    /**
     * Constructs a new InvalidDniForCodeGenerationException with
     * a detailed message.
     *
     * @param dni the invalid DNI value
     */
    public InvalidDniForCodeGenerationException(String dni) {
        super("The provided DNI '" + dni + "' is invalid for code generation.");
    }
}
