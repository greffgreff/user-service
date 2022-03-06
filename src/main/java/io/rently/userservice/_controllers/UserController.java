package io.rently.userservice._controllers;

import io.rently.userservice._services.UserService;
import io.rently.userservice.dtos.ResponseContent;
import io.rently.userservice.dtos.User;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class UserController implements ErrorController {

    @GetMapping(value = "/users/{id}")
    public static ResponseContent getUser(@PathVariable String id) {
        return UserService.returnUserById(id);
    }

    @PostMapping(value = "/users")
    public static ResponseContent addUser(@RequestBody User user) {
        return UserService.addUser(user);
    }

    @PutMapping(value = "/users/{id}")
    public static ResponseContent replaceUser(@PathVariable String id, @RequestBody User user) {
        return UserService.replaceUserById(id, user);
    }

    @DeleteMapping(value = "/users/{id}")
    public static ResponseContent deleteUser(@PathVariable String id) {
        return UserService.deleteUserById(id);
    }
}
