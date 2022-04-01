package io.rently.userservice.errors;

import io.rently.userservice.dtos.ResponseContent;
import io.rently.userservice.util.Broadcaster;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseContent handleGenericException(HttpServletResponse response, Exception exception) {
        Broadcaster.error(exception.getMessage());
        ResponseStatusException resEx = Errors.INTERNAL_SERVER_ERROR.getException();
        response.setStatus(resEx.getStatus().value());
        return new ResponseContent.Builder(resEx.getStatus()).setMessage(resEx.getMessage()).build();
    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseBody
    public static ResponseContent handleResponseException(HttpServletResponse response, ResponseStatusException ex) {
        Broadcaster.httpError(ex);
        response.setStatus(ex.getStatus().value());
        return new ResponseContent.Builder(ex.getStatus()).setMessage(ex.getReason()).build();
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    @ResponseBody
    public static ResponseContent handleInvalidURI(HttpServletResponse response) {
        ResponseStatusException resEx = Errors.INVALID_URI_PATH.getException();
        response.setStatus(resEx.getStatus().value());
        return new ResponseContent.Builder(resEx.getStatus()).setMessage(resEx.getMessage()).build();
    }
}
