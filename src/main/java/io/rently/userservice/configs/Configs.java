package io.rently.userservice.configs;

import io.rently.userservice.interfaces.IDatabaseContext;
import io.rently.userservice.persistency.SqlPersistence;
import io.rently.userservice.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Configs {

    @Bean
    public IDatabaseContext repositoryBean() {
        SqlPersistence sqlService = new SqlPersistence();
        sqlService.setUser("dbi433816");
        sqlService.setPassword("admin");
        sqlService.setServerName("studmysql01.fhict.local");
        sqlService.setDatabaseName("dbi433816");
        return sqlService;
    }

    @Bean
    public UserService userServiceBean() {
        return new UserService(repositoryBean());
    }
}
