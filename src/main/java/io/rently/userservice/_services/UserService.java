package io.rently.userservice._services;

import io.rently.userservice.dtos.ResponseContent;
import io.rently.userservice.dtos.User;
import io.rently.userservice.errors.enums.Errors;
import io.rently.userservice.interfaces.IDatabaseContext;
import io.rently.userservice.persistency.SqlPersistence;
import io.rently.userservice.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    private static final List<User> users = new ArrayList<User>(Arrays.asList(
            new User().setUsername("branlix2000").setFullName("Noah Greff").setEmail("noahgreff@gmail.com").setPhone("+31 56 41 84 27").createAsNew(),
            new User().setUsername("then00ber").setFullName("Chandler Greff").setEmail("chandlegreff@gmail.com").setPhone("+31 06 41 53 14").createAsNew(),
            new User().setUsername("chew_kok").setFullName("Chew kok").setPhone("+31 56 41 84 27").createAsNew()
    ));

//    private static final IDatabaseContext userRepository;

    public static ResponseContent returnUsers() {
        return new ResponseContent.Builder().setData(users).build();
    }

    public static ResponseContent returnUserById(String id) {
        User user = getUserById(id);
        return new ResponseContent.Builder().setData(user).build();
    }

    public static ResponseContent addUser(User user) {
        for (User existingUser: users) {
            handleUniqueProperties(user, existingUser);
        }
        users.add(user.createAsNew());
        return new ResponseContent.Builder().setMessage("Successfully added user with ID { id: " + user.getId() + " }").build();
    }

    public static ResponseContent deleteUserById(String id) {
        User user = getUserById(id);
        users.remove(user);
        return new ResponseContent.Builder().setMessage("Successfully removed user with ID { id: " + id + " }").build();
    }

    public static ResponseContent replaceUserById(String id, User userData) {
        User user = getUserById(id);

        for (User existingUser: users) {
            if (!Objects.equals(existingUser.getId(), id)) {
                handleUniqueProperties(userData, existingUser);
            }
        }

        users.remove(user);
        users.add(user.updateInfo(userData));

        return new ResponseContent.Builder().setMessage("Successfully updated user with ID { id: " + id + " }").build();
    }

    private static void handleUniqueProperties(User user, User existingUser) {
        if (Objects.equals(existingUser.getUsername(), user.getUsername()) || Objects.equals(existingUser.getEmail(), user.getEmail())) {
            throw Errors.USER_ALREADY_EXISTS.getException();
        }
    }

    private static User getUserById(String id) {
        for (User existingUser : users) {
            if (Objects.equals(existingUser.getId(), id)) {
                return existingUser;
            }
        }
        throw Errors.USER_NOT_FOUND.getException();
    }

//    static {
//        userRepository = new SqlPersistence("dbi433816", "admin", "studmysql01.fhict.local", "dbi433816");
//    }
}
