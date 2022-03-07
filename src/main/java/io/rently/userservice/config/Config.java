package io.rently.userservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("application.properties")
@ConfigurationProperties(prefix = "repository", ignoreUnknownFields=false)
public class Config {

    @Value("user")
    public String user;

    public void setUser(String user) {
        this.user = user;
    }
}