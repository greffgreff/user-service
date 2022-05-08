package io.rently.userservice.services;

import io.jsonwebtoken.SignatureAlgorithm;
import io.rently.userservice.utils.Broadcaster;
import io.rently.userservice.utils.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class MailerService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final Jwt jwt ;
    private final String endPointUrl;

    public MailerService(
            @Value("${mailer.secret}") String secret,
            @Value("${mailer.algo}") SignatureAlgorithm algo,
            @Value("${mailer.baseurl}") String endPointUrl
    ) {
        this.jwt = new Jwt(secret, algo);
        this.endPointUrl = endPointUrl;
    }

    public void dispatchGreeting(String recipientName, String recipientEmail) {
        Broadcaster.info("Sending greetings to user " + recipientName);
        Map<String, String> data = new HashMap<>();
        data.put("type", "GREETINGS");
        data.put("name", recipientName);
        data.put("email", recipientEmail);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt.generateBearToken());
        HttpEntity<Map<String, String>> body = new HttpEntity<>(data, headers);
        try {
            restTemplate.postForObject(endPointUrl + "api/v1/emails/dispatch/", body, String.class);
        } catch (Exception ex) {
            Broadcaster.warn("Could not send greetings to " + recipientEmail);
            Broadcaster.error(ex);
        }
    }

    public void dispatchGoodbye(String recipientName, String recipientEmail) {
        Broadcaster.info("Sending goodbyes to user " + recipientName);
        Map<String, String> data = new HashMap<>();
        data.put("type", "ACCOUNT_DELETION");
        data.put("name", recipientName);
        data.put("email", recipientEmail);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt.generateBearToken());
        HttpEntity<Map<String, String>> body = new HttpEntity<>(data, headers);
        try {
            restTemplate.postForObject(endPointUrl + "api/v1/emails/dispatch/", body, String.class);
        } catch (Exception ex) {
            Broadcaster.warn("Could not send goodbyes to " + recipientEmail);
            Broadcaster.error(ex);
        }
    }

    public void dispatchErrorToDevs(Exception exception) {
        Broadcaster.info("Dispatching error report...");
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
        try {
            restTemplate.postForObject(endPointUrl + "api/v1/emails/dispatch/", body, String.class);
        } catch (Exception ex) {
            Broadcaster.warn("Could not dispatch error report.");
            Broadcaster.error(ex);
        }
    }
}
