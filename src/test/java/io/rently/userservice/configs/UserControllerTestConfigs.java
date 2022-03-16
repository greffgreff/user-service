package io.rently.userservice.configs;

import io.rently.userservice.interfaces.IDatabaseContext;
import io.rently.userservice.services.UserService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfigs {

    @Bean
    @Primary
    public IDatabaseContext repositoryBeanTest() {
        return Mockito.mock(IDatabaseContext.class);
    }

    @Bean
    @Primary
    public UserService userServiceBeanTest() {
        return Mockito.mock(UserService.class);
    }
}
