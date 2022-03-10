package io.rently.userservice.configs;

import io.rently.userservice.interfaces.IDatabaseContext;
import io.rently.userservice.services.UserService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfigs {

    @Bean
    public IDatabaseContext repositoryBeanTest() {
        return Mockito.mock(IDatabaseContext.class);
    }

    @Bean
    public UserService userServiceBeanTest() {
        return Mockito.mock(UserService.class);
    }
}
