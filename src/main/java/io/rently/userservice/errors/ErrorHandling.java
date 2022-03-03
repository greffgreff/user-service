package io.rently.userservice.errors;

import io.rently.userservice.dtos.ResponseContent;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ErrorHandling {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static ResponseContent handleRouteNotFound() {
        return new ResponseContent.Builder(HttpStatus.BAD_REQUEST).setMessage("Invalid or incomplete URL path").build();
    }

    @ExceptionHandler(HttpException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public static ResponseContent handleGenericHttpException(HttpException ex) {
        return new ResponseContent.Builder(ex.getStatus()).setMessage(ex.getMessage()).build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static ResponseContent handleNotFound(NotFoundException ex) {
        return new ResponseContent.Builder(ex.getStatus()).setMessage(ex.getMessage()).build();
    }

    @ExceptionHandler(NotFoundException.UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static ResponseContent handleUserNotFound(NotFoundException.UserNotFoundException ex) {
        return new ResponseContent.Builder(ex.getStatus()).setMessage(ex.getMessage()).build();
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public static ResponseContent handleConflict(ConflictException ex) {
        return new ResponseContent.Builder(ex.getStatus()).setMessage(ex.getMessage()).build();
    }

    @ExceptionHandler(ConflictException.UserConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public static ResponseContent handleUserConflict(ConflictException.UserConflictException ex) {
        return new ResponseContent.Builder(ex.getStatus()).setMessage(ex.getMessage()).build();
    }
}
