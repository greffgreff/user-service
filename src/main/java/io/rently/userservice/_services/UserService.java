package io.rently.userservice._services;

import io.rently.userservice.dtos.ResponseContent;
import io.rently.userservice.dtos.User;
import io.rently.userservice.errors.ConflictException;
import io.rently.userservice.errors.NotFoundException;
import io.rently.userservice.interfaces.IDatabaseContext;
import io.rently.userservice.persistency.SqlPersistence;
import io.rently.userservice.util.Util;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UserService {
    private static final List<User> users = new ArrayList<User>(Arrays.asList(
            new User("1", "branlix2000", "Noah Greff", "noahgreff@gmail.com", "+31 06 41 53 14", Util.getCurrentTs(), Util.getCurrentTs()),
            new User("2", "then00ber", "Chandler Greff", "chandlegreff@gmail.com", "+31 56 41 84 27", Util.getCurrentTs(), Util.getCurrentTs()),
            new User("3", "chew_kok", "Chew kok", "noahgreff@gmail.com", null, Util.getCurrentTs(), Util.getCurrentTs())
    ));

//    private final IDatabaseContext<User> repository;
//    public UserService(IDatabaseContext<User> repository) {
//        this.repository = repository;
//    }

    public static ResponseContent returnUsers() {
        return new ResponseContent.Builder().setData(users).build();
    }

    public static ResponseContent returnUserById(String id) {
        User user = getUserById(id);
        return new ResponseContent.Builder().setData(user).build();
    }

    public static ResponseContent addUser(User user) {
        handleUserExistence(user.getId());
        for (User existingUser: users) {
            handleUniqueProperties(user, existingUser);
        }
        user.refreshCreationDate();
        users.add(user);
        return new ResponseContent.Builder().setMessage("Successfully added user with ID { id: " + user.getId() + " }").build();
    }

    public static ResponseContent deleteUserById(String id) {
        User user = getUserById(id);
        users.remove(user);
        return new ResponseContent.Builder().setMessage("Successfully removed user with ID { id: " + id + " }").build();
    }

    public static ResponseContent replaceUserById(String id, User userData) {
        User oldUser = getUserById(id);

        for (User existingUser: users) {
            if (!Objects.equals(existingUser.getId(), id)) {
                handleUniqueProperties(userData, existingUser);
            }
        }

        User updatedUser = new User(id);
        updatedUser
                .setUsername(Util.getNonNull(userData.getUsername(), oldUser.getUsername()))
                .setFullName(Util.getNonNull(userData.getFullName(), oldUser.getFullName()))
                .setEmail(Util.getNonNull(userData.getEmail(), oldUser.getEmail()))
                .setPhone(Util.getNonNull(userData.getPhone(), oldUser.getPhone()))
                .setCreatedOn(oldUser.getCreatedOn())
                .refreshUpdateDate();

        users.remove(oldUser);
        users.add(updatedUser);

        return new ResponseContent.Builder().setMessage("Successfully updated user with ID { id: " + id + " }").build();
    }

    private static void handleUniqueProperties(User user, User existingUser) {
        if (Objects.equals(existingUser.getUsername(), user.getUsername())) {
            throw new ConflictException.UserConflictException(User.class.getDeclaredFields()[1], user.getUsername());
        } else if (Objects.equals(existingUser.getEmail(), user.getEmail())) {
            throw new ConflictException.UserConflictException(User.class.getDeclaredFields()[3], user.getEmail());
        }
    }

    private static void handleUserExistence(String id) {
        for (User existingUser : users) {
            if (Objects.equals(existingUser.getId(), id)) {
                throw new ConflictException.UserConflictException(User.class.getDeclaredFields()[0], id);
            }
        }
    }

    private static User getUserById(String id) {
        for (User existingUser : users) {
            if (Objects.equals(existingUser.getId(), id)) {
                return existingUser;
            }
        }
        throw new NotFoundException.UserNotFoundException(User.class.getDeclaredFields()[0], id);
    }
}
