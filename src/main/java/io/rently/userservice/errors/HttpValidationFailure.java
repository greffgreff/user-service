package io.rently.userservice.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class HttpValidationFailure extends ResponseStatusException {

    public HttpValidationFailure(String fieldName, Class<?> fieldType, String value) {
        super(HttpStatus.BAD_REQUEST, "Validation failure occurred. Value of field \'" + fieldName + "\' could not be recognized as type " + fieldType.getName() + " (value: \"" + value + "\")" );
    }
}
