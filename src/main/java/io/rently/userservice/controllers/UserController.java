package io.rently.userservice.controllers;

import io.rently.userservice.dtos.ResponseContent;
import io.rently.userservice.dtos.User;
import io.rently.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class UserController implements ErrorController {
    public final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users/id/{id}")
    public ResponseContent getUserById(@PathVariable String id) {
        User user = service.returnUserById(id);
        return new ResponseContent.Builder().setData(user).build();
    }

    @GetMapping("/users/username/{username}")
    public ResponseContent getUserByUsername(@PathVariable String username) {
        List<User> users = service.returnUsersByUsername(username);
        return new ResponseContent.Builder().setData(users).build();
    }

    @GetMapping("/users/email/{email}")
    public ResponseContent getUserByEmail(@PathVariable String email) {
        List<User> users = service.returnUsersByEmail(email);
        return new ResponseContent.Builder().setData(users).build();
    }

    @PostMapping("/users")
    public ResponseContent addUser(@RequestBody User userData) {
        service.addUser(userData);
        return new ResponseContent.Builder().setMessage("Successfully added user to database").build();
    }

    @PutMapping("/users/{id}")
    public ResponseContent replaceUser(@PathVariable String id, @RequestBody User userData) {
        service.updateUserById(id, userData);
        return new ResponseContent.Builder().setMessage("Successfully updated user in database").build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseContent deleteUser(@PathVariable String id) {
        service.deleteUserById(id);
        return new ResponseContent.Builder().setMessage("Successfully delete user from database").build();
    }
}

// 3bdb141f-deb6-4260-a8ac-999e6ab9c89d
