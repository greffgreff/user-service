package io.rently.userservice._services;

import io.rently.userservice.dtos.ResponseContent;
import io.rently.userservice.dtos.User;
import io.rently.userservice.errors.enums.Errors;
import io.rently.userservice.interfaces.IDatabaseContext;
import io.rently.userservice.persistency.SqlPersistence;
import io.rently.userservice.util.Broadcaster;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {
    private final IDatabaseContext repository = new SqlPersistence("dbi433816", "admin", "studmysql01.fhict.local", "dbi433816");

    // 9aef044d-6549-4785-9234-cb7f9314777a
    // 0269aec5-21cb-4b19-9fe1-90e1d5595dd9
    // 747af12c-6be0-4dfe-8964-f447305d6737
    public ResponseContent returnUserById(String id) {
        User user;
        try {
            user = repository.getById(User.class, id);
        }
        catch (Exception ex) {
            Broadcaster.error("An error occurred while attempting to get user: " + ex.getMessage());
            throw Errors.INTERNAL_SERVER_ERROR.getException();
        }

        if (user == null) {
            Broadcaster.info("User not found (ID: " + id + ")");
            throw Errors.USER_NOT_FOUND.getException();
        }

        return new ResponseContent.Builder().setData(user).build();
    }

    public ResponseContent addUser(User userData) {
        if (userData.getEmail() == null || userData.getUsername() == null || userData.getPassword() == null) { }
        User user = userData.createAsNew();
        repository.add(user);
        Broadcaster.info("User added to database (ID: " + user.getId() + ")");
        return new ResponseContent.Builder().setData(user).setMessage("Successfully added user to database").build();
    }

    public ResponseContent deleteUserById(String id) {
//        User user = (User) repository.get(User.class.getDeclaredAnnotation(PersistentField.class), id);
//        repository.delete(user);
        Broadcaster.info("User removed from database (ID: " + id + ")");
        return new ResponseContent.Builder().setMessage("Successfully removed user with ID { id: " + id + " }").build();
    }

    public ResponseContent replaceUserById(String id, User userData) {
//        User user = (User) repository.get();
//        repository.delete(user);
//        repository.add(user.updateInfo(userData));
        Broadcaster.info("User information update (ID: " + id + ")");
        return new ResponseContent.Builder().setMessage("Successfully updated user with ID { id: " + id + " }").build();
    }

    private void handleUniqueProperties(User user, User existingUser) {
        if (Objects.equals(existingUser.getUsername(), user.getUsername()) || Objects.equals(existingUser.getEmail(), user.getEmail())) {
            throw Errors.USER_ALREADY_EXISTS.getException();
        }
    }
}
