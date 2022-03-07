package io.rently.userservice.controllers;

import io.rently.userservice.dtos.ResponseContent;
import io.rently.userservice.dtos.User;
import io.rently.userservice.services.UserService;
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
        return userService.updateUserById(id, user);
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseContent deleteUser(@PathVariable String id) {
        return userService.deleteUserById(id);
    }
}

// 9aef044d-6549-4785-9234-cb7f9314777a
// 0269aec5-21cb-4b19-9fe1-90e1d5595dd9
// 747af12c-6be0-4dfe-8964-f447305d6737