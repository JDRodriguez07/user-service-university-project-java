package edu.university.user_service.exceptions;

public class RoleNotFoundException extends RuntimeException {

    /**
     * Constructor para RoleNotFoundException.
     *
     * @param roleName El nombre del rol que no fue encontrado.
     */
    public RoleNotFoundException(String roleName) {
        super("Role not found with name: " + roleName);
    }

}
