package io.rently.userservice.service;

import io.rently.userservice.dto.ResponseBody;
import io.rently.userservice.dto.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UserService {
    private static final List<User> users = new ArrayList<>(Arrays.asList(
            new User.Builder("1").setUsername("branlix2000").setFullname("Noah Greff").setEmail("something@gmail.com").setPhone("+31 06 41 53 14").build(),
            new User.Builder("2").setUsername("than00ber").setFullname("Chandler Greff").setEmail("something.other@yahoo.com").build(),
            new User.Builder("3").setUsername("chew kok").setFullname("Chew Kok").setEmail("chew.kok@hotmail.com").build()
    ));

    public static ResponseBody getUsers() { // add query param
        return new ResponseBody
                .Builder(new Timestamp(System.currentTimeMillis()), 200)
                .setData(users)
                .build();
    }

    public static ResponseBody getUserById(String id) { // add uuid check
        for (User user: users) {
            if (Objects.equals(user.getId(), id)) {
                return new ResponseBody
                        .Builder(new Timestamp(System.currentTimeMillis()), 200)
                        .setData(user)
                        .build();
            }
        }

        return new ResponseBody
                .Builder(new Timestamp(System.currentTimeMillis()), 404)
                .setData("User with id " + id + " not found")
                .build();
    }

    public static ResponseBody addUser(User user) { // check user id existence
        users.add(user);
        return new ResponseBody
                .Builder(new Timestamp(System.currentTimeMillis()), 200)
                .setData("Successfully added user with id " + user.getId())
                .build();
    }

    public static ResponseBody deleteUserById(String id) { // add uuid check
        for (User user: users.stream().toList()) {
            if (Objects.equals(user.getId(), id)) {
                users.remove(user);

                return new ResponseBody
                        .Builder(new Timestamp(System.currentTimeMillis()), 200)
                        .setData("Successfully removed user with id " + id)
                        .build();
            }
        }

        return new ResponseBody
                .Builder(new Timestamp(System.currentTimeMillis()), 404)
                .setData("User with id " + id + " not found")
                .build();
    }

    public static ResponseBody updateUserById(String id) { // add uuid check, check info
        for (User user: users.stream().toList()) {
            if (Objects.equals(user.getId(), id)) {
                users.remove(user);
                users.add(new User
                        .Builder(user.getId())
                        .build()
                );

                return new ResponseBody
                        .Builder(new Timestamp(System.currentTimeMillis()), 200)
                        .setData("Successfully updated user with id " + id)
                        .build();
            }
        }

        return new ResponseBody
                .Builder(new Timestamp(System.currentTimeMillis()), 404)
                .setData("User with id " + id + " not found")
                .build();
    }
}
