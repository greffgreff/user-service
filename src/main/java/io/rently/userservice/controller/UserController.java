package io.rently.userservice.controller;

import io.rently.userservice.dto.User;
import io.rently.userservice.dto.ResponseBody;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.UUID;

@RestController
public class UserController implements ErrorController {
    public static final String PREFIX = "api/v1";

    @RequestMapping("/error")
    public static ResponseBody error() {
        return new ResponseBody
                .Builder(new Timestamp(System.currentTimeMillis()), 500)
                .setData("Invalid or incomplete URL path")
                .build();
    }

    @GetMapping(value = PREFIX + "/users")
    public static ResponseBody getUser() {
        Object data = new User
                .Builder(UUID.randomUUID().toString())
                .setUsername("branlix2000")
                .setFullname("Noah Greff")
                .setEmail("something@gmail.com")
                .setPhone("+31 06 41 53 14")
                .build();

        ResponseBody response = new ResponseBody
                .Builder(new Timestamp(System.currentTimeMillis()), 200)
                .setData(data)
                .build();

        return response;
    }

    @PostMapping(value = PREFIX + "/users")
    public static ResponseBody addUser() {
        return new ResponseBody
                .Builder(new Timestamp(System.currentTimeMillis()), 201)
                .setData("POST request")
                .build();
    }

    @PutMapping(value = PREFIX + "/users/user")
    public static ResponseBody updatedUser() {
        return new ResponseBody
                .Builder(new Timestamp(System.currentTimeMillis()), 201)
                .setData("PUT request")
                .build();
    }

    @DeleteMapping(value = PREFIX + "/users/user")
    public static ResponseBody deleteUser() {
        return new ResponseBody
                .Builder(new Timestamp(System.currentTimeMillis()), 201)
                .setData("DELETE request")
                .build();
    }
}
