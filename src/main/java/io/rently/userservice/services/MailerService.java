package io.rently.userservice.services;

import io.rently.userservice.utils.Broadcaster;
import io.rently.userservice.utils.Jwt;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class MailerService {

    private final RestTemplate restTemplate;
    private final Jwt jwt;
    private final String endPointUrl;

    public MailerService(Jwt jwt, String baseUrl, RestTemplate restTemplate) {
        this.jwt = jwt;
        this.endPointUrl = baseUrl + "api/v1/emails/dispatch/";
        this.restTemplate = restTemplate;
    }

    public void dispatchGreeting(String recipientName, String recipientEmail) {
        Broadcaster.info("Sending greetings to user " + recipientName);
        Objects.requireNonNull(recipientEmail, "Email cannot be null");

        Map<String, String> data = new HashMap<>();
        data.put("type", "GREETINGS");
        data.put("name", recipientName);
        data.put("email", recipientEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt.generateBearToken());
        HttpEntity<Map<String, String>> body = new HttpEntity<>(data, headers);

        restTemplate.postForObject(endPointUrl, body, String.class);
    }

    public void dispatchGoodbye(String recipientName, String recipientEmail) {
        Broadcaster.info("Sending goodbyes to user " + recipientName);
        Objects.requireNonNull(recipientEmail, "Email cannot be null");

        Map<String, String> data = new HashMap<>();
        data.put("type", "ACCOUNT_DELETION");
        data.put("name", recipientName);
        data.put("email", recipientEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt.generateBearToken());
        HttpEntity<Map<String, String>> body = new HttpEntity<>(data, headers);

        restTemplate.postForObject(endPointUrl, body, String.class);
    }

    public void dispatchErrorToDevs(Exception exception) {
        Broadcaster.info("Dispatching error report...");
        Objects.requireNonNull(exception, "Empty exception provided");

        Map<String, Object> report = new HashMap<>();
        report.put("type", "DEV_ERROR");
        report.put("datetime", new Date());
        report.put("message", exception.getMessage());
        report.put("service", "User service");
        report.put("cause", exception.getCause());
        report.put("trace", Arrays.toString(exception.getStackTrace()));
        report.put("exceptionType", exception.getClass());

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt.generateBearToken());
        HttpEntity<Map<String, Object>> body = new HttpEntity<>(report, headers);

        restTemplate.postForObject(endPointUrl, body, String.class);
    }
}
