package io.rently.userservice.controller;

import io.rently.userservice.dto.ResponseBody;
import io.rently.userservice.service.UserService;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
public class UserController implements ErrorController {
    public static final String PREFIX = "api/v1";

    @GetMapping(value = PREFIX + "/users")
    public static ResponseBody getUsers() {
        return UserService.getUsers();
    }

    @GetMapping(value = PREFIX + "/users/{id}")
    public static ResponseBody getUser(@PathVariable String id) {
        return UserService.getUserById(id);
    }

    @PostMapping(value = PREFIX + "/users")
    public static ResponseBody addUser() {
        return UserService.addUser();
    }

    @PutMapping(value = PREFIX + "/users/{id}")
    public static ResponseBody updatedUser(@PathVariable String id) {
        return UserService.updateUserById(id);
    }

    @DeleteMapping(value = PREFIX + "/users/{id}")
    public static ResponseBody deleteUser(@PathVariable String id) {
        return UserService.deleteUserById(id);
    }

    @RequestMapping("/error")
    public static ResponseBody handleErrorResponse() {
        return new ResponseBody
                .Builder(new Timestamp(System.currentTimeMillis()), 500)
                .setData("Invalid or incomplete URL path")
                .build();
    }
}
