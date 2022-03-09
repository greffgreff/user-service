package io.rently.userservice.controllers;

import io.rently.userservice.dtos.ResponseContent;
import io.rently.userservice.dtos.User;
import io.rently.userservice.services.UserService;
import io.rently.userservice.util.Broadcaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class UserController implements ErrorController {
    public final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users/{id}")
    public ResponseContent getUser(@PathVariable String id) {
        User user = userService.returnUserById(id);
        return new ResponseContent.Builder().setData(user).build();
    }

    @PostMapping(value = "/users")
    public ResponseContent addUser(@RequestBody(required = false) User userData) {
        userService.addUser(userData);
        return new ResponseContent.Builder().setMessage("Successfully added user to database").build();
    }

    @PutMapping(value = "/users/{id}")
    public ResponseContent replaceUser(@PathVariable String id, @RequestBody(required = false) User userData) {
        userService.updateUserById(id, userData);
        return new ResponseContent.Builder().setMessage("Successfully updated user in database").build();
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseContent deleteUser(@PathVariable String id) {
        userService.deleteUserById(id);
        return new ResponseContent.Builder().setMessage("Successfully delete user from database").build();
    }
}

// 9aef044d-6549-4785-9234-cb7f9314777a
