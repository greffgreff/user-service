package io.rently.userservice.controllers;

import io.rently.userservice.dtos.ResponseContent;
import io.rently.userservice.dtos.User;
import io.rently.userservice.services.UserService;
import io.rently.userservice.util.Broadcaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class UserController implements ErrorController {
    public static final String PREFIX = "/api/v2";

    @Autowired
    public UserService service;

    @GetMapping(PREFIX + "/{id}")
    public ResponseContent handleGetRequest(@PathVariable String id) {
        User user = service.getUserById(id);
        return new ResponseContent.Builder().setData(user).build();
    }

    @GetMapping(PREFIX + "/{provider}/{providerId}")
    public ResponseContent handleGetRequest(@PathVariable String provider, @PathVariable String providerId) {
        User user = service.getUserByProvider(provider, providerId);
        return new ResponseContent.Builder().setData(user).build();
    }

    @PostMapping(PREFIX + "/")
    public ResponseContent handlePostRequest(@RequestBody User user) {
        service.addUser(user);
        return new ResponseContent.Builder().setMessage("Successfully added user to database").build();
    }

    @PutMapping(PREFIX + "/{id}")
    public ResponseContent handlePutRequest(@RequestHeader("Authorization") String header, @PathVariable String id, @RequestBody User user) {
        service.verifyOwnership(header, user);
        service.updateUser(id, user);
        return new ResponseContent.Builder().setData("Successfully updated user from database").build();
    }

    @DeleteMapping(PREFIX + "/{id}")
    public ResponseContent handleDeleteRequest(@RequestHeader("Authorization") String header, @PathVariable String id) {
        service.verifyOwnership(header, id);
        service.deleteUser(id);
        return new ResponseContent.Builder().setData("Successfully removed user from database").build();
    }
}
