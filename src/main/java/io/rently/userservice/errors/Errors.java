package io.rently.userservice.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public enum Errors {
    USER_NOT_FOUND(new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with specified arguments")),
    DATABASE_CONNECTION_FAILED(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to establish connection to database")),
    INVALID_URI_PATH(new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Invalid or incomplete URI")),
    INTERNAL_SERVER_ERROR(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Request could not be processed due to an internal server error")),
    USERNAME_NOT_FOUND(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username unset")),
    NO_DATA(new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No content found in request body")),
    INVALID_DATA(new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid data provided by client")),
    PAYLOAD_TOO_LARGE(new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "One or more fields exceeds 100 characters"));

    private final ResponseStatusException exception;

    Errors(ResponseStatusException exception) {
        this.exception = exception;
    }

    public ResponseStatusException getException() {
        return exception;
    }
}
