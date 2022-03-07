package io.rently.userservice.services;

import io.rently.userservice.dtos.ResponseContent;
import io.rently.userservice.dtos.User;
import io.rently.userservice.errors.enums.Errors;
import io.rently.userservice.interfaces.IDatabaseContext;
import io.rently.userservice.persistency.SqlPersistence;
import io.rently.userservice.util.Broadcaster;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final IDatabaseContext repository = new SqlPersistence("dbi433816", "admin", "studmysql01.fhict.local", "dbi433816");

    // 9aef044d-6549-4785-9234-cb7f9314777a
    // 0269aec5-21cb-4b19-9fe1-90e1d5595dd9
    // 747af12c-6be0-4dfe-8964-f447305d6737
    public ResponseContent returnUserById(String id) {
        User user = getUserById(id);
        if (user == null) {
            Broadcaster.info("User not found (ID: " + id + ")");
            throw Errors.USER_NOT_FOUND.getException();
        }
        return new ResponseContent.Builder().setData(user).build();
    }

    public ResponseContent addUser(User userData) {
        handleUniqueValueCheck(userData);
        User user = userData.createAsNew();
        repository.add(user);
        Broadcaster.info("User added to database (ID: " + user.getId() + ")");
        return new ResponseContent.Builder().setData(user).setMessage("Successfully added user to database").build();
    }

    public ResponseContent deleteUserById(String id) {
        User user = getUserById(id);
        if (user == null) {
            Broadcaster.info("User not found (ID: " + id + ")");
            throw Errors.USER_NOT_FOUND.getException();
        }
        repository.delete(user);
        Broadcaster.info("User removed from database (ID: " + id + ")");
        return new ResponseContent.Builder().setMessage("Successfully removed user with ID { id: " + id + " }").build();
    }

    public ResponseContent updateUserById(String id, User userData) {
        User user = getUserById(id);
        if (user == null) {
            Broadcaster.info("User not found (ID: " + id + ")");
            throw Errors.USER_NOT_FOUND.getException();
        }
        handleUniqueValueCheck(userData);
        repository.update(user);
        Broadcaster.info("User information update (ID: " + id + ")");
        return new ResponseContent.Builder().setMessage("Successfully updated user with ID { id: " + id + " }").build();
    }

    private User getUserById(String id) {
        try {
            return repository.getById(User.class, id);
        }
        catch (Exception ex) {
            Broadcaster.error("An error occurred while attempting to get user: " + ex.getMessage());
            throw Errors.INTERNAL_SERVER_ERROR.getException();
        }
    }

    private List<User> getUsersByKey(String key, String value) {
        try {
            return repository.get(User.class, key, value);
        }
        catch (Exception ex) {
            Broadcaster.error("An error occurred while attempting to get user: " + ex.getMessage());
            throw Errors.INTERNAL_SERVER_ERROR.getException();
        }
    }

    private void handleUniqueValueCheck(User userData) {
        if (!getUsersByKey("email", userData.getEmail()).isEmpty()) {
            Broadcaster.info("User if email already exists (Email: " + userData.getEmail() + ")");
            throw Errors.EMAIL_ALREADY_EXISTS.getException();
        }
        if (!getUsersByKey("username", userData.getEmail()).isEmpty()) {
            Broadcaster.info("User if email already exists (Email: " + userData.getEmail() + ")");
            throw Errors.USERNAME_ALREADY_EXISTS.getException();
        }
    }
}
