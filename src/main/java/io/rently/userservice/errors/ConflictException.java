package io.rently.userservice.errors;

import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;

public class ConflictException extends HttpException {

    public ConflictException() {
        this("Resource conflict has occurred");
    }

    public ConflictException(String message) {
        super(HttpStatus.CONFLICT, message);
    }

    public static class UserConflictException extends ConflictException {

        public UserConflictException(Field field, Object value) {
            super(String.format("User with with matching unique property already exists { %s = %s }", field.getName(), value));
        }
    }
}
