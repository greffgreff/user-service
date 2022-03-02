package io.rently.userservice.controller;

import io.rently.userservice.dto.ResponseContent;
import io.rently.userservice.dto.User;
import io.rently.userservice.service.UserService;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class UserController implements ErrorController {

    @GetMapping(value = "/users")
    public static ResponseContent getUsers() {
        return UserService.getUsers();
    }

    @GetMapping(value = "/users/{id}")
    public static ResponseContent getUser(@PathVariable String id) {
        return UserService.getUserById(id);
    }

    @PostMapping(value = "/users")
    public static ResponseContent addUser(@RequestBody User user) {
        return UserService.addUser(user);
    }

    @PutMapping(value = "/users/{id}")
    public static ResponseContent updatedUser(@PathVariable String id) {
        return UserService.updateUserById(id);
    }

    @DeleteMapping(value = "/users/{id}")
    public static ResponseContent deleteUser(@PathVariable String id) {
        return UserService.deleteUserById(id);
    }
}
