package io.rently.userservice.services;

import io.rently.userservice.configs.UserServiceTestConfigs;
import io.rently.userservice.dtos.User;
import io.rently.userservice.errors.enums.Errors;
import io.rently.userservice.interfaces.IDatabaseContext;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(UserService.class)
@ContextConfiguration(classes = UserServiceTestConfigs.class)
class UserServiceTest {

    @Autowired
    private IDatabaseContext repository;

    @Autowired
    private UserService service;

    @Test
    void returnUserById_invalidId_userNotFoundThrown() {
        String userId = "invalidUserId";
        Exception expected = Errors.USER_NOT_FOUND.getException();

        Mockito.when(repository.getById(User.class, userId))
                .thenReturn(null);

        Exception ex = assertThrows(expected.getClass(),
                () -> service.returnUserById(userId));

        assertEquals(ex.getMessage(), expected.getMessage());
    }

    @Test
    void returnUserById_validId_userReturned() {
        String userId = "validUserId";
        User expected = new User().createAsNew();

        Mockito.when(repository.getById(User.class, userId))
                .thenReturn(expected);

        User user = service.returnUserById(userId);

        assertEquals(user, expected);
    }

    @Test
    void addUser_noUserData_noDataThrown() {
        Exception expected = Errors.NO_DATA.getException();

        Exception ex = assertThrows(expected.getClass(),
                () -> service.addUser(null));

        assertEquals(ex.getMessage(), expected.getMessage());
    }

    @Test
    void addUser_noEmail_noEmailThrown() {
        User userData = new User.Builder()
                .setEmail(null)
                .setPassword("password")
                .setUsername("username")
                .build();
        Exception expected = Errors.EMAIL_NOT_FOUND.getException();

        Exception ex = assertThrows(expected.getClass(),
                () -> service.addUser(userData));

        assertEquals(ex.getMessage(), expected.getMessage());
    }

    @Test
    void addUser_noUsername_noUsernameThrown() {
        User userData = new User.Builder()
                .setEmail("email")
                .setPassword("password")
                .setUsername(null)
                .build();
        Exception expected = Errors.USERNAME_NOT_FOUND.getException();

        Exception ex = assertThrows(expected.getClass(),
                () -> service.addUser(userData));

        assertEquals(ex.getMessage(), expected.getMessage());
    }

    @Test
    void addUser_noPassword_noPasswordThrown() {
        User userData = new User.Builder()
                .setEmail("email")
                .setPassword(null)
                .setUsername("username")
                .build();
        Exception expected = Errors.PASSWORD_NOT_FOUND.getException();

        Exception ex = assertThrows(expected.getClass(),
                () -> service.addUser(userData));

        assertEquals(ex.getMessage(), expected.getMessage());
    }

    @Test
    void addUser_validUserData_existingEmail_emailExistThrown() {
        User userData = new User.Builder()
                .setId("123")
                .setEmail("existingEmail")
                .setPassword("password")
                .setUsername("username")
                .build();
        User existingUser = new User.Builder()
                .setId("abc")
                .setEmail("existingEmail")
                .build();
        Exception expected = Errors.EMAIL_ALREADY_EXISTS.getException();

        Mockito.when(repository.get(User.class, "email", userData.getEmail()))
                .thenReturn(List.of(existingUser));

        Exception ex = assertThrows(expected.getClass(),
                () -> service.addUser(userData));

        assertEquals(ex.getMessage(), expected.getMessage());
    }

    @Test
    void addUser_validUserData_existingUsername_usernameExistThrown() {
        User userData = new User.Builder()
                .setId("123")
                .setEmail("email")
                .setPassword("password")
                .setUsername("existingUsername")
                .build();
        User existingUser = new User.Builder()
                .setId("abc")
                .setUsername("existingUsername")
                .build();
        Exception expected = Errors.USERNAME_ALREADY_EXISTS.getException();

        Mockito.when(repository.get(User.class, "username", userData.getUsername()))
                .thenReturn(List.of(existingUser));

        Exception ex = assertThrows(expected.getClass(),
                () -> service.addUser(userData));

        assertEquals(ex.getMessage(), expected.getMessage());
    }

    @Test
    void addUser_validUserData_success() {
        User userData = new User.Builder()
                .setId("123")
                .setEmail("email")
                .setPassword("password")
                .setUsername("username")
                .build();

        assertDoesNotThrow(() -> service.addUser(userData));
    }

    @Test
    void deleteUserById_invalidId_userNotFoundThrown() {
        String userId = "invalidUserId";
        Exception expected = Errors.USER_NOT_FOUND.getException();

        Mockito.when(repository.getById(User.class, userId))
                .thenReturn(null);

        Exception ex = assertThrows(expected.getClass(),
                () -> service.deleteUserById(userId));

        assertEquals(ex.getMessage(), expected.getMessage());
    }

    @Test
    void deleteUserById_validId_success() {
        String userId = "validId";
        User existingUser = new User.Builder()
                .setId(userId)
                .setEmail("email")
                .setPassword("password")
                .setUsername("username")
                .build();

        Mockito.when(repository.getById(User.class, userId))
                .thenReturn(existingUser);

        assertDoesNotThrow(() -> service.deleteUserById(userId));
    }

    @Test
    void updateUserById_noUserData_noDataThrown() {
        Exception expected = Errors.NO_DATA.getException();

        Exception ex = assertThrows(expected.getClass(),
                () -> service.updateUserById(null, null));

        assertEquals(ex.getMessage(), expected.getMessage());
    }

    @Test
    void updateUserById_validUserData_invalidUserId_userNotFoundThrown() {
        String userId = "invalidId";
        User userData = new User.Builder()
                .setId(userId)
                .setEmail("email")
                .setPassword("password")
                .setUsername("username")
                .build();
        Exception expected = Errors.USER_NOT_FOUND.getException();

        Mockito.when(repository.getById(User.class, userId))
                .thenReturn(null);

        Exception ex = assertThrows(expected.getClass(),
                () -> service.updateUserById(userId, userData));

        assertEquals(ex.getMessage(), expected.getMessage());
    }

    @Test
    void updateUserById_validUserData_validUserId_emailAlreadyExists_emailExistsThrown() {
        String userId = "123";
        User userData = new User.Builder()
                .setId(userId)
                .setEmail("existingEmail")
                .setPassword("password")
                .setUsername("username")
                .build();
        User existingUser = new User.Builder()
                .setId("abc")
                .setEmail("existingEmail")
                .build();
        Exception expected = Errors.EMAIL_ALREADY_EXISTS.getException();

        Mockito.when(repository.getById(User.class, userId))
                .thenReturn(userData);

        Mockito.when(repository.get(User.class, "email", userData.getEmail()))
                .thenReturn(List.of(existingUser));

        Exception ex = assertThrows(expected.getClass(),
                () -> service.updateUserById(userId, userData));

        assertEquals(ex.getMessage(), expected.getMessage());
    }

    @Test
    void updateUserById_validUserData_validUserId_usernameAlreadyExists_usernameExistsThrown() {
        String userId = "123";
        User userData = new User.Builder()
                .setId(userId)
                .setEmail("email")
                .setPassword("password")
                .setUsername("existingUsername")
                .build();
        User existingUser = new User.Builder()
                .setId("abc")
                .setUsername("existingUsername")
                .build();
        Exception expected = Errors.USERNAME_ALREADY_EXISTS.getException();

        Mockito.when(repository.getById(User.class, userId))
                .thenReturn(userData);

        Mockito.when(repository.get(User.class, "username", userData.getUsername()))
                .thenReturn(List.of(existingUser));

        Exception ex = assertThrows(expected.getClass(),
                () -> service.updateUserById(userId, userData));

        assertEquals(ex.getMessage(), expected.getMessage());
    }

    @Test
    void updateUserById_validUserData_validUserId_success() {
        String userId = "123";
        User userData = new User.Builder()
                .setId(userId)
                .setEmail("email")
                .setPassword("password")
                .setUsername("existingUsername")
                .build();

        Mockito.when(repository.get(User.class, "username", userData.getUsername()))
                .thenReturn(new ArrayList<>());

        Mockito.when(repository.get(User.class, "email", userData.getEmail()))
                .thenReturn(new ArrayList<>());

        Mockito.when(repository.getById(User.class, userId))
                .thenReturn(userData);

        assertDoesNotThrow(() -> service.updateUserById(userId, userData));
    }
}