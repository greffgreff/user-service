package io.rently.userservice.controllers;

import io.rently.userservice.services.UserService;
import io.rently.userservice.dtos.ResponseContent;
import io.rently.userservice.dtos.User;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class UserController implements ErrorController {
    public final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users/{id}")
    public ResponseContent getUser(@PathVariable String id) {
        return userService.returnUserById(id);
    }

    @PostMapping(value = "/users")
    public ResponseContent addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping(value = "/users/{id}")
    public ResponseContent replaceUser(@PathVariable String id, @RequestBody User user) {
        return userService.replaceUserById(id, user);
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseContent deleteUser(@PathVariable String id) {
        return userService.deleteUserById(id);
    }
}
