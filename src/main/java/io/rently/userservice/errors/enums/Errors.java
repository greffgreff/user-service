package io.rently.userservice.errors.enums;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

public enum Errors {
    NOT_FOUND(new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found")),
    USER_NOT_FOUND(new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with specified arguments")),
    CONFLICT(new ResponseStatusException(HttpStatus.CONFLICT, "Resource conflict occurred")),
    USER_ALREADY_EXISTS(new ResponseStatusException(HttpStatus.CONFLICT, "User with matching unique properties already exists"));

    private final ResponseStatusException exception;

    private Errors(ResponseStatusException exception) {
        this.exception = exception;
    }

    public ResponseStatusException getException() {
        return exception;
    }
}
