package io.rently.userservice._controllers;

import io.rently.userservice._services.UserService;
import io.rently.userservice.dtos.ResponseContent;
import io.rently.userservice.dtos.User;
import io.rently.userservice.errors.HttpException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class UserController implements ErrorController {

    @GetMapping(value = "/users")
    public static ResponseContent getUsers() {
        return UserService.returnUsers();
    }

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

    @PatchMapping(value = "/users/{id}")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public static ResponseContent updatedUser(@PathVariable String id, @RequestBody User user) {
        throw new HttpException(HttpStatus.NOT_IMPLEMENTED, "PATCH request not implemented");
    }

    @DeleteMapping(value = "/users/{id}")
    public static ResponseContent deleteUser(@PathVariable String id) {
        return UserService.deleteUserById(id);
    }
}
