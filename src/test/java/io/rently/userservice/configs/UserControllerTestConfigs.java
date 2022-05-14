package io.rently.userservice.configs;

import com.bugsnag.Bugsnag;
import io.jsonwebtoken.SignatureAlgorithm;
import io.rently.userservice.interfaces.UserRepository;
import io.rently.userservice.services.UserService;
import io.rently.userservice.utils.Jwt;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class UserControllerTestConfigs {

    @Bean
    @Primary
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }
}
