package edu.university.user_service.exceptions;

public class StudentNotFoundException extends RuntimeException {

    /**
     * Constructor for StudentNotFoundException.
     *
     * @param id the ID of the student that was not found
     */
    public StudentNotFoundException(Long id) {
        super("Student with ID " + id + " was not found.");
    }
}
