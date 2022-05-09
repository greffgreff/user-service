package io.rently.userservice.errors;

import com.bugsnag.Bugsnag;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.rently.userservice.services.MailerService;
import io.rently.userservice.dtos.ResponseContent;
import io.rently.userservice.utils.Broadcaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ExceptionController {

    @Autowired
    private Bugsnag bugsnag;
    @Autowired
    private MailerService mailer;

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseContent unhandledException(HttpServletResponse response, Exception exception) {
        Broadcaster.error(exception.getMessage());
        ResponseStatusException resEx = Errors.INTERNAL_SERVER_ERROR;
        bugsnag.notify(exception);
        try {
            mailer.dispatchErrorToDevs(exception);
        } catch (Exception ex) {
            Broadcaster.warn("Could not dispatch error report.");
            Broadcaster.error(ex);
        }
        response.setStatus(resEx.getStatus().value());
        return new ResponseContent.Builder(resEx.getStatus()).setMessage(resEx.getReason()).build();
    }

    @ResponseBody
    @ExceptionHandler(ResponseStatusException.class)
    public static ResponseContent responseException(HttpServletResponse response, ResponseStatusException ex) {
        Broadcaster.httpError(ex);
        response.setStatus(ex.getStatus().value());
        return new ResponseContent.Builder(ex.getStatus()).setMessage(ex.getReason()).build();
    }

    @ResponseBody
    @ExceptionHandler({ MethodNotAllowedException.class, HttpRequestMethodNotSupportedException.class })
    public static ResponseContent invalidURI(HttpServletResponse response) {
        ResponseStatusException resEx = Errors.INVALID_URI_PATH;
        response.setStatus(resEx.getStatus().value());
        return new ResponseContent.Builder(resEx.getStatus()).setMessage(resEx.getReason()).build();
    }

    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public static ResponseContent invalidFormatException(HttpServletResponse response) {
        ResponseStatusException respEx = Errors.NO_DATA;
        response.setStatus(respEx.getStatus().value());
        return new ResponseContent.Builder(respEx.getStatus()).setMessage(respEx.getReason()).build();
    }
}
