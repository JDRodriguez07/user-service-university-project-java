package edu.university.user_service.exceptions;

public class TeacherNotFoundException extends RuntimeException {

    /**
     * Constructor for TeacherNotFoundException.
     *
     * @param id the ID of the teacher that was not found
     */
    public TeacherNotFoundException(Long id) {
        super("Teacher with ID " + id + " was not found.");
    }
}
