package io.rently.userservice.configs;

import io.rently.userservice.interfaces.IDatabaseContext;
import io.rently.userservice.persistency.SqlPersistence;
import io.rently.userservice.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class Configs {

    @Primary
    @Bean
    public IDatabaseContext repositoryBean() {
        SqlPersistence sqlService = new SqlPersistence();
        sqlService.setUser("dbi433816");
        sqlService.setPassword("admin");
        sqlService.setServerName("studmysql01.fhict.local");
        sqlService.setDatabaseName("dbi433816");
        return sqlService;
    }

    @Primary
    @Bean
    public UserService userServiceBean() {
        return new UserService(repositoryBean());
    }
}
