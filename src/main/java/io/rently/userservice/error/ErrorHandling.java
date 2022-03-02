package io.rently.userservice.error;

import io.rently.userservice.dto.ResponseContent;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.Timestamp;

@ControllerAdvice
public class ErrorHandling extends RuntimeException {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static @ResponseBody ResponseContent handleRouteNotFound() {
        return new ResponseContent
                .Builder(HttpStatus.NOT_FOUND)
                .setMessage("Invalid or incomplete URL path")
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static @ResponseBody ResponseContent handleNotFound(NotFoundException ex) {
        return new ResponseContent
                .Builder(HttpStatus.NOT_FOUND)
                .setMessage(ex.getMessage())
                .build();
    }

    @ExceptionHandler(NotFoundException.UserByIdNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static @ResponseBody ResponseContent handleUserNotFound(NotFoundException.UserByIdNotFound ex) {
        return new ResponseContent
                .Builder(HttpStatus.NOT_FOUND)
                .setMessage(ex.getMessage())
                .build();
    }
}
