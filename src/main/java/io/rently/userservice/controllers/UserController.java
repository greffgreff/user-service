package io.rently.userservice.controllers;

import io.rently.userservice.dtos.ResponseContent;
import io.rently.userservice.dtos.User;
import io.rently.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class UserController implements ErrorController {
    public final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users/{id}")
    public ResponseContent getUser(@PathVariable String id) {
        User user = service.returnUserById(id);
        return new ResponseContent.Builder().setData(user).build();
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

// 9aef044d-6549-4785-9234-cb7f9314777a
