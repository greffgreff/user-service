package io.rently.userservice.services;

import io.rently.userservice.dtos.User;
import io.rently.userservice.errors.enums.Errors;
import io.rently.userservice.interfaces.IDatabaseContext;
import io.rently.userservice.util.Broadcaster;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    private final IDatabaseContext repository;

    public UserService(IDatabaseContext repository) {
        this.repository = repository;
    }

    public List<User> returnUsersByUsername(String username) {
        return getUsersByUsername(username);
    }

    public List<User> returnUsersByEmail(String email) {
        return getUsersByEmail(email);
    }

    public User returnUserById(String id) {
        return getUserById(id);
    }

    public void addUser(User userData) {
        checkUserData(userData);
        checkUniqueValues(userData, null);
        if (userData.getUsername() == null) {
            throw Errors.USERNAME_NOT_FOUND.getException();
        }
        if (userData.getEmail() == null) {
            throw Errors.EMAIL_NOT_FOUND.getException();
        }
        if (userData.getPassword() == null) {
            throw Errors.PASSWORD_NOT_FOUND.getException();
        }
        User user = userData.createAsNew();
        repository.add(user);
        Broadcaster.info("User added to database (ID: " + user.getId() + ")");
    }

    public void deleteUserById(String id) {
        User user = getUserById(id);
        repository.delete(user);
        Broadcaster.info("User removed from database (ID: " + user.getId() + ")");
    }

    public void updateUserById(String id, User userData) {
        checkUserData(userData);
        User user = getUserById(id);
        checkUniqueValues(userData, id);
        repository.update(user.updateInfo(userData));
        Broadcaster.info("User information update (ID: " + user.getId() + ")");
    }

    private User getUserById(String id) {
        User user = repository.getById(User.class, id);
        if (user != null) return user;
        Broadcaster.info("User not found (ID: " + id + ")");
        throw Errors.USER_NOT_FOUND.getException();
    }

    private List<User> getUsersByEmail(String email) {
        return repository.get(User.class, "email", email);
    }

    private List<User> getUsersByUsername(String username) {
        return repository.get(User.class, "username", username);
    }

    private void checkUniqueValues(User userData, String id) {
        for (User user : getUsersByEmail(userData.getEmail())) {
            if (!Objects.equals(user.getId(), id)) {
                Broadcaster.info("Email already exists (Email: " + userData.getEmail() + ")");
                throw Errors.EMAIL_ALREADY_EXISTS.getException();
            }
        }
        for (User user : getUsersByUsername(userData.getUsername())) {
            if (!Objects.equals(user.getId(), id)) {
                Broadcaster.info("Username already exists (Username: " + userData.getUsername() + ")");
                throw Errors.USERNAME_ALREADY_EXISTS.getException();
            }
        }
    }

    private void checkUserData(User userData) {
        if (userData == null) {
            Broadcaster.info("No data in request body found");
            throw Errors.NO_DATA.getException();
        }
    }
}
