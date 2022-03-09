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
    public ResponseContent addUser(@RequestBody User userData) {
        User user = userService.addUser(userData);
        Broadcaster.info("User added to database (ID: " + user.getId() + ")");
        return new ResponseContent.Builder().setMessage("Successfully added user (ID: " + user.getId() + ")").build();
    }

    @PutMapping(value = "/users/{id}")
    public ResponseContent replaceUser(@PathVariable String id, @RequestBody User userData) {
        User user = userService.updateUserById(id, userData);
        Broadcaster.info("User information update (ID: " + user.getId() + ")");
        return new ResponseContent.Builder().setMessage("Successfully updated user (ID: " + user.getId() + ")").build();
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseContent deleteUser(@PathVariable String id) {
        User user = userService.deleteUserById(id);
        Broadcaster.info("User removed from database (ID: " + user.getId() + ")");
        return new ResponseContent.Builder().setMessage("Successfully delete user (ID: " + user.getId() + ")").build();
    }
}

// 9aef044d-6549-4785-9234-cb7f9314777a
