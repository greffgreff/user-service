package io.rently.userservice.services;

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

    public UserService() { } // FIXME add DI

    public ResponseContent returnUserById(String id) {
        User user = getUserById(id);
        return new ResponseContent.Builder().setData(user).build();
    }

    public ResponseContent addUser(User userData) {
        checkUniqueValues(userData, null);
        User user = userData.createAsNew();
        repository.add(user);
        Broadcaster.info("User added to database (ID: " + user.getId() + ")");
        return new ResponseContent.Builder().setData(user).setMessage("Successfully added user to database (ID: " + user.getId() + ")").build();
    }

    public ResponseContent deleteUserById(String id) {
        User user = getUserById(id);
        repository.delete(user);
        Broadcaster.info("User removed from database (ID: " + id + ")");
        return new ResponseContent.Builder().setMessage("Successfully removed user (ID: " + id + ")").build();
    }

    public ResponseContent updateUserById(String id, User userData) {
        User user = getUserById(id);
        checkUniqueValues(userData, id);
        repository.update(user.updateInfo(userData));
        Broadcaster.info("User information update (ID: " + id + ")");
        return new ResponseContent.Builder().setMessage("Successfully updated user (ID: " + id + ")").build();
    }

    private User getUserById(String id) {
        User user = repository.getById(User.class, id);
        if (user != null) return user;
        Broadcaster.info("User not found (ID: " + id + ")");
        throw Errors.USER_NOT_FOUND.getException();
    }

    private void checkUniqueValues(User userData, String id) {
        for (User user : repository.get(User.class, "email", userData.getEmail())) {
            if (!Objects.equals(user.getId(), id)) {
                Broadcaster.info("User if email already exists (Email: " + userData.getEmail() + ")");
                throw Errors.EMAIL_NOT_FOUND.getException();
            }
        }
        for (User user : repository.get(User.class, "username", userData.getUsername())) {
            if (!Objects.equals(user.getId(), id)) {
                Broadcaster.info("User if username already exists (Username: " + userData.getUsername() + ")");
                throw Errors.USERNAME_ALREADY_EXISTS.getException();
            }
        }
    }
}
