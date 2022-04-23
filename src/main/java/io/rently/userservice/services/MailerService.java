package io.rently.userservice.services;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class MailerService {
    public static final String BASE_URL = "http://localhost:8084/api/v1/dispatch/";
    private static final RestTemplate restTemplate = new RestTemplate();

    public static void dispatchGreeting(String recipientName, String recipientEmail) {
        Map<String, String> data = new HashMap<>();
        data.put("type", "GREETINGS");
        data.put("name", recipientName);
        data.put("email", recipientEmail);
        restTemplate.postForObject(BASE_URL, data, String.class);
    }

    public static void dispatchGoodbye(String recipientName, String recipientEmail) {
        Map<String, String> data = new HashMap<>();
        data.put("type", "ACCOUNT_DELETION");
        data.put("name", recipientName);
        data.put("email", recipientEmail);
        restTemplate.postForObject(BASE_URL, data, String.class);
    }

    public static void dispatchErrorToDevs(Exception exception) {
        Map<String, Object> data = new HashMap<>();
        data.put("type", "DEV_ERROR");
        data.put("datetime", new Date());
        data.put("message", exception.getMessage());
        data.put("service", "Mailer service");
        data.put("cause", exception.getCause());
        data.put("trace", exception.getStackTrace());
        data.put("exceptionType", exception.getClass());
        restTemplate.postForObject(BASE_URL, data, String.class);
    }
}
