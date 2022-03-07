package io.rently.userservice.errors.enums;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public enum Errors {
    NOT_FOUND(new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found")),
    USER_NOT_FOUND(new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with specified arguments")),
    CONFLICT(new ResponseStatusException(HttpStatus.CONFLICT, "Resource conflict occurred")),
    USER_ALREADY_EXISTS(new ResponseStatusException(HttpStatus.CONFLICT, "User with matching unique property already exists")),
    DATABASE_CONNECTION_FAILED(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to establish connection to database")),
    INVALID_URL_PATH(new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid or incomplete request")),
    INTERNAL_SERVER_ERROR(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Request could not be processed due to an internal server error"));

    private final ResponseStatusException exception;

    Errors(ResponseStatusException exception) {
        this.exception = exception;
    }

    public ResponseStatusException getException() {
        return exception;
    }
}
