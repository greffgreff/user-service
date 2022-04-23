package io.rently.userservice.services;

import io.rently.userservice.utils.Broadcaster;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class MailerService {
    public static final String BASE_URL = "http://localhost:8084/api/v1/emails/dispatch/";
    private static final RestTemplate restTemplate = new RestTemplate();

    public static void dispatchGreeting(String recipientName, String recipientEmail) {
        Map<String, String> data = new HashMap<>();
        data.put("type", "GREETINGS");
        data.put("name", recipientName);
        data.put("email", recipientEmail);
        restTemplate.postForObject(BASE_URL, data, String.class);
    }

    public static void dispatchGoodbye(String recipientName, String recipientEmail) {
        Broadcaster.info("Sending greetings to user " + recipientName);
        Map<String, String> data = new HashMap<>();
        data.put("type", "ACCOUNT_DELETION");
        data.put("name", recipientName);
        data.put("email", recipientEmail);
        restTemplate.postForObject(BASE_URL, data, String.class);
    }

    public static void dispatchErrorToDevs(Exception exception) {
        Broadcaster.info("Dispatching error report...");
        Map<String, Object> report = new HashMap<>();
        report.put("type", "DEV_ERROR");
        report.put("datetime", new Date());
        report.put("message", exception.getMessage());
        report.put("service", "Mailer service");
        report.put("cause", exception.getCause());
        report.put("trace", Arrays.toString(exception.getStackTrace()));
        report.put("exceptionType", exception.getClass());
        restTemplate.postForObject(BASE_URL, report, String.class);
    }
}
