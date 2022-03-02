package io.rently.userservice.error;

import io.rently.userservice.dto.ResponseContent;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorHandling extends RuntimeException {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static @ResponseBody ResponseContent handleRouteNotFound() {
        return new ResponseContent
                .Builder(HttpStatus.BAD_REQUEST)
                .setMessage("Invalid or incomplete URL path")
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static @ResponseBody ResponseContent handleNotFound(NotFoundException ex) {
        return new ResponseContent
                .Builder(ex.getStatus())
                .setMessage(ex.getMessage())
                .build();
    }

    @ExceptionHandler(NotFoundException.UserNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static @ResponseBody ResponseContent handleUserNotFound(NotFoundException.UserNotFound ex) {
        return new ResponseContent
                .Builder(ex.getStatus())
                .setMessage(ex.getMessage())
                .build();
    }
}
