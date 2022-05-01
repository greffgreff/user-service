package io.rently.userservice.services;

import io.rently.userservice.interfaces.UserRepository;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class UserServiceTestConfigs {

    @Bean
    @Primary
    public UserRepository repositoryBeanTest() {
        return Mockito.mock(UserRepository.class);
    }
}
