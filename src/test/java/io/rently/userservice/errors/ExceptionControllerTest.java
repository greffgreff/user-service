package io.rently.userservice.errors;

import com.bugsnag.Bugsnag;
import io.rently.userservice.components.MailerService;
import io.rently.userservice.configs.ExceptionControllerTestConfigs;
import io.rently.userservice.dtos.ResponseContent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@WebMvcTest(ExceptionController.class)
@ContextConfiguration(classes = ExceptionControllerTestConfigs.class)
class ExceptionControllerTest {

    public MockHttpServletResponse response;
    @Autowired
    public ExceptionController controller;
    @Autowired
    public MailerService mailer;
    @Autowired
    public Bugsnag bugsnag;

    @BeforeEach
    void setup() {
        response = new MockHttpServletResponse();
    }

    @Test
    void unhandledException_mailerInvoked_bugsnagInvoked_void() {
        Exception exception = new Exception("This is an unhandled exception");

        ResponseContent content = controller.unhandledException(response, exception);

        verify(mailer, times(1)).dispatchErrorToDevs(
                argThat(ex -> {
                    assert Objects.equals(ex.getMessage(), exception.getMessage());
                    return true;
                })
        );

        verify(bugsnag, times(1)).notify(
                (Throwable) argThat(thrw -> {
                    assert thrw.getClass() == exception.getClass();
                    return true;
                })
        );

        ResponseStatusException expectedException = Errors.INTERNAL_SERVER_ERROR;
        assert response.getStatus() == expectedException.getStatus().value();
        assert content.getStatus() == expectedException.getStatus().value();
        assert Objects.requireNonNull(expectedException.getMessage()).contains(content.getMessage());
    }

    @Test
    void responseException() {
        ResponseStatusException exception = new ResponseStatusException(HttpStatus.BAD_REQUEST, "This is a bad request exception");

        ResponseContent content = ExceptionController.responseException(response, exception);

        assert response.getStatus() == exception.getStatus().value();
        assert content.getStatus() == exception.getStatus().value();
        assert Objects.requireNonNull(exception.getMessage()).contains(content.getMessage());
    }

    @Test
    void invalidURI() {
        ResponseStatusException expectedException = Errors.INVALID_URI_PATH;

        ResponseContent content = ExceptionController.invalidURI(response);

        assert response.getStatus() == expectedException.getStatus().value();
        assert content.getStatus() == expectedException.getStatus().value();
        assert Objects.requireNonNull(expectedException.getMessage()).contains(content.getMessage());
    }

    @Test
    void invalidFormatException() {
        ResponseStatusException expectedException = Errors.NO_DATA;

        ResponseContent content = ExceptionController.invalidFormatException(response);

        assert response.getStatus() == expectedException.getStatus().value();
        assert content.getStatus() == expectedException.getStatus().value();
        assert Objects.requireNonNull(expectedException.getMessage()).contains(content.getMessage());
    }

    @Test
    void missingResource() {
        MissingRequestValueException exception = new MissingRequestValueException("A field is missing in the header...");

        ResponseContent content = ExceptionController.missingResource(response, exception);

        assert response.getStatus() == HttpStatus.NOT_ACCEPTABLE.value();
        assert content.getStatus() == HttpStatus.NOT_ACCEPTABLE.value();
        assert ("Missing resource: " +  exception.getMessage()).contains(content.getMessage());
    }
}