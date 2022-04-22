package io.rently.userservice.apis;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
}
