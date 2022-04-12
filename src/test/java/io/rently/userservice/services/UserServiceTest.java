package io.rently.userservice.services;

import io.rently.userservice.interfaces.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserService.class)
@ContextConfiguration(classes = UserServiceTestConfigs.class)
class UserServiceTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository repository;

    @Test
    void getUserByProvider() {
    }

    @Test
    void getUserById() {
    }

    @Test
    void addUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void tryFindUserByProvider() {
    }

    @Test
    void tryFindUserById() {
    }

    @Test
    void verifyOwnership() {
    }

    @Test
    void testVerifyOwnership() {
    }

    @Test
    void validateData() {
    }
}