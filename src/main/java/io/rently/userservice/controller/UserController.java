package io.rently.userservice.controller;

import io.rently.userservice.dto.ResponseBody;
import io.rently.userservice.dto.User;
import io.rently.userservice.service.UserService;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
public class UserController implements ErrorController {
    public static final String BASE_ROUTE = "api/v1";

    @GetMapping(value = BASE_ROUTE + "/users")
    public static ResponseBody getUsers() {
        return UserService.getUsers();
    }

    @GetMapping(value = BASE_ROUTE + "/users/{id}")
    public static ResponseBody getUser(@PathVariable String id) {
        return UserService.getUserById(id);
    }

    @PostMapping(value = BASE_ROUTE + "/users")
    public static ResponseBody addUser(@RequestBody User user) {
        return UserService.addUser(user);
    }

    @PutMapping(value = BASE_ROUTE + "/users/{id}")
    public static ResponseBody updatedUser(@PathVariable String id) {
        return UserService.updateUserById(id);
    }

    @DeleteMapping(value = BASE_ROUTE + "/users/{id}")
    public static ResponseBody deleteUser(@PathVariable String id) {
        return UserService.deleteUserById(id);
    }

    @RequestMapping("/error")
    public static ResponseBody handleErrorResponse() {
        return new ResponseBody
                .Builder(new Timestamp(System.currentTimeMillis()), 500) // wrong
                .setData("An error occurred")
                .build();
    }
}
