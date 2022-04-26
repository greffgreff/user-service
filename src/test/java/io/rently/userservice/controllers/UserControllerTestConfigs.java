package io.rently.userservice.controllers;

import com.bugsnag.Bugsnag;
import io.rently.userservice.interfaces.UserRepository;
import io.rently.userservice.services.UserService;
import io.rently.userservice.utils.Jwt;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class UserControllerTestConfigs {

    @Bean
    @Primary
    public Jwt jwtBeanTest() { return Mockito.mock(Jwt.class); }

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

    @Bean
    @Primary
    public Bugsnag bugsnagTestBean() { return Mockito.mock(Bugsnag.class); }

}