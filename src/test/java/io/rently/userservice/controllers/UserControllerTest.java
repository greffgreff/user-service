package io.rently.userservice.controllers;

import io.rently.userservice.configs.UserControllerTestConfigs;
import io.rently.userservice.dtos.User;
import io.rently.userservice.interfaces.UserRepository;
import io.rently.userservice.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = UserControllerTestConfigs.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
class UserControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    UserRepository repository;

    @Test
    void handleGetRequest_validUserId_returnUser() throws Exception {
        String validUserId = "validUserId";
        User user = new User();
        user.setId("validUserId");

        doReturn(Optional.of(user)).when(repository).findById(validUserId);

        ResultActions response = mvc.perform(get("/api/v2/users/{id}", validUserId));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        assert responseJson.contains(String.valueOf(HttpStatus.OK.value()));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").doesNotExist());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content.id").isNotEmpty());
        assert responseJson.contains(validUserId);
    }

    @Test
    void testHandleGetRequest() {
    }

    @Test
    void handlePostRequest() {
    }

    @Test
    void handlePutRequest() {
    }

    @Test
    void handleDeleteRequest() {
    }
}