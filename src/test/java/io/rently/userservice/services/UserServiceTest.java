package io.rently.userservice.services;

import io.rently.userservice.configs.BugsnagTestConfigs;
import io.rently.userservice.configs.UserServiceTestConfigs;
import io.rently.userservice.dtos.User;
import io.rently.userservice.errors.Errors;
import io.rently.userservice.interfaces.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@WebMvcTest(UserService.class)
@ContextConfiguration(classes = { UserServiceTestConfigs.class, BugsnagTestConfigs.class})
class UserServiceTest {

    @Autowired
    public UserService service;
    @Autowired
    public UserRepository repository;

    User getValidGenericUser() {
        User validUserData = new User();
        validUserData.setId(UUID.randomUUID().toString());
        validUserData.setProvider("provider");
        validUserData.setProviderId("providerId");
        validUserData.setName("name");
        validUserData.setEmail("email");
        validUserData.setUpdatedAt(String.valueOf(new Date().getTime()));
        validUserData.setCreatedAt(String.valueOf(new Date().getTime()));
        return validUserData;
    }

    @Test
    void getUserByProvider_invalidProviderInfo_throwNotFound() {
        String provider = "invalid";
        String providerId = "invalid";

        when(repository.findByProviderAndProviderId(provider, providerId))
                .thenReturn(Optional.ofNullable(null));

        assertThrows(Errors.USER_NOT_FOUND.getClass(), () -> service.getUserByProvider(provider, providerId));
    }

    @Test
    void getUserByProvider_validProviderInfo_returnUser() {
        String provider = "discord";
        String providerId = "124124123123";
        User expectedUser = new User();
        expectedUser.setProvider(provider);
        expectedUser.setProviderId(providerId);

        when(repository.findByProviderAndProviderId(provider, providerId))
                .thenReturn(Optional.of(expectedUser));

        User userFound = service.getUserByProvider(provider, providerId);

        assert userFound == expectedUser;
    }

    @Test
    void getUserById_invalidUserId_throwNotFound() {
        String id = "invalid";

        when(repository.findById(id))
                .thenReturn(Optional.ofNullable(null));

        assertThrows(Errors.USER_NOT_FOUND.getClass(), () -> service.getUserById(id));
    }

    @Test
    void getUserById_validUserId_returnUser() {
        String id = "124124123123";
        User expectedUser = new User();
        expectedUser.setId(id);

        when(repository.findById(id))
                .thenReturn(Optional.of(expectedUser));

        User userFound = service.getUserById(id);

        assert userFound == expectedUser;
    }

    @Test
    void addUser_existingUserProviderInfo_throwAlreadyExists() {
        String provider = "discord";
        String providerId = "124124123123";
        User existingUser = new User();
        existingUser.setProviderId(providerId);
        existingUser.setProvider(provider);

        when(repository.findByProviderAndProviderId(provider, providerId))
                .thenReturn(Optional.of(existingUser));

        assertThrows(Errors.USER_ALREADY_EXISTS.getClass(), () -> service.addUser(existingUser));
    }

    @Test
    void addUser_existingUserId_throwAlreadyExists() {
        String id = "124124123123";
        User existingUser = new User();
        existingUser.setId(id);

        when(repository.findById(id))
                .thenReturn(Optional.of(existingUser));

        assertThrows(Errors.USER_ALREADY_EXISTS.getClass(), () -> service.addUser(existingUser));
    }

    @Test
    void addUser_validUserData_trySendGreetings() {
        User validUserData = getValidGenericUser();

        assertDoesNotThrow(() -> service.addUser(validUserData));
    }

    @Test
    void updateUser_invalidUserId_throwNotFound() {
        String invalidDataHolderId = "invalid";
        User user = getValidGenericUser();

        when(repository.findById(invalidDataHolderId))
                .thenReturn(Optional.ofNullable(null));

        assertThrows(Errors.USER_NOT_FOUND.getClass(), () -> service.updateUser(invalidDataHolderId, user));
    }

    @Test
    void updateUser_validUserData_void() {
        User validUserData = getValidGenericUser();
        String validId = validUserData.getId();

        when(repository.findById(validId))
                .thenReturn(Optional.of(validUserData));

        assertDoesNotThrow(() -> service.updateUser(validUserData.getId(), validUserData));
    }

    @Test
    void deleteUser_invalidId_throwNotFound() {
        String invalidDataHolderId = "invalid";

        when(repository.findById(invalidDataHolderId))
                .thenReturn(Optional.ofNullable(null));

        assertThrows(Errors.USER_NOT_FOUND.getClass(), () -> service.deleteUser(invalidDataHolderId));
    }

    @Test
    void deleteUser_validId_trySendGoodbyes() {
        User validUserData = getValidGenericUser();
        String validId = validUserData.getId();

        when(repository.findById(validId))
                .thenReturn(Optional.of(validUserData));

        assertDoesNotThrow(() -> service.deleteUser(validId));
    }
}