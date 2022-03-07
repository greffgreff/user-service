package io.rently.userservice.errors;

import io.rently.userservice.dtos.ResponseContent;
import io.rently.userservice.errors.enums.Errors;
import io.rently.userservice.util.Broadcaster;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletResponse;

@EnableWebMvc
@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(ResponseStatusException.class)
    public static ResponseContent handleResponseException(HttpServletResponse response, ResponseStatusException ex) {
        response.setStatus(ex.getStatus().value());
        return new ResponseContent.Builder(ex.getStatus()).setMessage(ex.getReason()).build();
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public static ResponseContent handleInvalidPath(HttpServletResponse response, NoHandlerFoundException ex) {
        ResponseStatusException respEx = Errors.INVALID_URL_PATH.getException();
        response.setStatus(respEx.getStatus().value());
        Broadcaster.info("Invalid request path (PATH: " + ex.getRequestURL() + ", METHOD: " + ex.getHttpMethod() + ")");
        return new ResponseContent.Builder(respEx.getStatus()).setMessage(respEx.getReason()).build();
    }
}
