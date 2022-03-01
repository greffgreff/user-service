package io.rently.userservice.service;

import io.rently.userservice.dto.ResponseBody;
import io.rently.userservice.dto.User;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class UserService {
    private static final List<User> users = Arrays.asList(
            new User
                    .Builder("1")
                    .setUsername("branlix2000")
                    .setFullname("Noah Greff")
                    .setEmail("something@gmail.com")
                    .setPhone("+31 06 41 53 14")
                    .build(),

            new User
                    .Builder(UUID.randomUUID().toString())
                    .setUsername("than00ber")
                    .setFullname("Chandler Greff")
                    .setEmail("something.other@yahoo.com")
                    .build(),

            new User
                    .Builder(UUID.randomUUID().toString())
                    .setUsername("chew kok")
                    .setFullname("Chew Kok")
                    .setEmail("chew.kok@hotmail.com")
                    .build()
    );

    public static ResponseBody getUsers() {
        return new ResponseBody
                .Builder(new Timestamp(System.currentTimeMillis()), 200)
                .setData(users)
                .build();
    }

    public static ResponseBody getUserById(String id) {
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
}
