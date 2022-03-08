package io.rently.userservice.errors.enums;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public enum Errors {
    USER_NOT_FOUND(new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with specified arguments")),
    EMAIL_ALREADY_EXISTS(new ResponseStatusException(HttpStatus.CONFLICT, "User with matching email already exists")),
    USERNAME_ALREADY_EXISTS(new ResponseStatusException(HttpStatus.CONFLICT, "User with matching username already exists")),
    DATABASE_CONNECTION_FAILED(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to establish connection to database")),
    INVALID_URL_PATH(new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid or incomplete request")),
    INTERNAL_SERVER_ERROR(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Request could not be processed due to an internal server error")),
    USERNAME_NOT_FOUND(new ResponseStatusException(HttpStatus.NOT_FOUND, "Username unset")),
    EMAIL_NOT_FOUND(new ResponseStatusException(HttpStatus.NOT_FOUND, "Email address unset")),
    PASSWORD_NOT_FOUND(new ResponseStatusException(HttpStatus.NOT_FOUND, "Password unset")),
    PAYLOAD_TOO_LARGE(new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "One or more fields exceeds 100 characters"));

    private final ResponseStatusException exception;

    Errors(ResponseStatusException exception) {
        this.exception = exception;
    }

    public ResponseStatusException getException() {
        return exception;
    }
}
