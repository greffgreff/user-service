package io.rently.userservice._service;

import io.rently.userservice.dto.ResponseContent;
import io.rently.userservice.dto.User;
import io.rently.userservice.error.ConflictException;
import io.rently.userservice.error.NotFoundException;
import io.rently.userservice.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UserService {
    private static final List<User> users = new ArrayList<>(Arrays.asList(
            new User.Builder("1").setUsername("branlix2000").setFullName("Noah Greff").setEmail("something@gmail.com").setPhone("+31 06 41 53 14").build(),
            new User.Builder("2").setUsername("than00ber").setFullName("Chandler Greff").setEmail("something.other@yahoo.com").build(),
            new User.Builder("3").setUsername("chew kok").setFullName("Chew Kok").setEmail("chew.kok@hotmail.com").build()
    ));

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
        users.remove(oldUser);
        users.add(new User
                .Builder(id)
                .setUsername(Util.getNonNull(userData.getUsername(), oldUser.getUsername()))
                .setFullName(Util.getNonNull(userData.getFullName(), oldUser.getFullName()))
                .setEmail(Util.getNonNull(userData.getEmail(), oldUser.getEmail()))
                .setPhone(Util.getNonNull(userData.getPhone(), oldUser.getPhone()))
                .build()
        );
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
