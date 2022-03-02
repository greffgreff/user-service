package io.rently.userservice.error;

import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;

public class ConflitException extends HttpException {

    public ConflitException() {
        this("Resource conflict has occurred");
    }

    public ConflitException(String message) {
        super(HttpStatus.CONFLICT, message);
    }

    public static class UserConflictException extends ConflitException {

        public UserConflictException(Field field, Object value) {
            super(String.format("User with with matching unique property found { %s = %s }", field.getName(), value));
        }
    }
}
