package io.rently.userservice.controllers;

import io.rently.userservice.interfaces.UserRepository;
import io.rently.userservice.services.UserService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class UserControllerTestConfigs {

    @Bean
    @Primary
    public UserRepository repositoryBeanTest() {
        return Mockito.mock(UserRepository.class);
    }

    @Bean
    @Primary
    public UserService userServiceBeanTest() {
        return Mockito.mock(UserService.class);
    }
}