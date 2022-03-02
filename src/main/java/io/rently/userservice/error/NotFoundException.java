package io.rently.userservice.error;

import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;

public class NotFoundException extends HttpException {

    public NotFoundException() {
        this("Resource could not be found");
    }

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public static class UserNotFoundException extends NotFoundException {

        public UserNotFoundException(Field field, Object value) {
            super(String.format("Could not find user with specified argument { %s = %s }", field.getName(), value));
        }
    }
}
