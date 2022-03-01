package io.rently.userservice.controller;

import io.rently.userservice.dto.User;
import io.rently.userservice.dto.ResponseBody;
import io.rently.userservice.service.UserService;
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
    public static ResponseBody getUsers() {
        return UserService.getUsers();
    }

    @GetMapping(value = PREFIX + "/users/{id}")
    public static ResponseBody getUser(@PathVariable(required = false) String id) {
        return UserService.getUserById(id);
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
