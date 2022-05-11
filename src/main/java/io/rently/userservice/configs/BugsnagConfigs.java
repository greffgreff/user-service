package io.rently.userservice.configs;

import com.bugsnag.Bugsnag;
import com.bugsnag.BugsnagSpringConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(BugsnagSpringConfiguration.class)
public class BugsnagConfigs {

    @Bean
    public Bugsnag bugsnag(@Value("${bugsnag.key}") String key) {
        return new Bugsnag(key);
    }
}