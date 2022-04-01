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

    @GetMapping(PREFIX + "/{provider}/{id}")
    public ResponseContent handleGetRequest(@PathVariable String provider, @PathVariable String id) {
        User user = service.getUser(provider, id);
        return new ResponseContent.Builder().setData(user).build();
    }

    @PutMapping(PREFIX + "/{provider}/{id}")
    public ResponseContent handlePutRequest(@PathVariable String provider, @PathVariable String id) {
        User user = service.updateUser(provider, id);
        return new ResponseContent.Builder().setData(user).build();
    }

    @PostMapping(PREFIX + "/")
    public ResponseContent handlePostRequest(@RequestBody User user) {
        service.addUser(user);
        return new ResponseContent.Builder().setMessage("Successfully added user to database.").build();
    }

    @DeleteMapping(PREFIX + "/{provider}/{id}")
    public ResponseContent handleDeleteRequest(@PathVariable String provider, @PathVariable String id) {
        service.deleteUser(provider, id);
        return new ResponseContent.Builder().setData("Successfully removed user from database.").build();
    }
}
