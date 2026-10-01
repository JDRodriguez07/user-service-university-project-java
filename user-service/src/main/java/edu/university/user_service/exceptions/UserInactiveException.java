package edu.university.user_service.exceptions;

import edu.university.user_service.enums.UserStatus;

public class UserInactiveException extends RuntimeException {

    public UserInactiveException(UserStatus status) {
        super("User is not allowed to log in. Current status: " + status);
    }
}
