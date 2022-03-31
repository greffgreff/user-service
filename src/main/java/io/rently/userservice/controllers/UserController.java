package io.rently.userservice.controllers;

import io.rently.userservice.dtos.ResponseContent;
import io.rently.userservice.dtos.User;
import io.rently.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class UserController implements ErrorController {
    public static final String PREFIX = "/api/v2";

    @Autowired
    public UserService service;

    @GetMapping("/**")
    public RedirectView redirectGets() {
        return new RedirectView(PREFIX + "/");
    }

    @PutMapping("/**")
    public RedirectView redirectPuts() {
        return new RedirectView(PREFIX + "/");
    }

    @PostMapping("/**")
    public RedirectView redirectPosts() {
        return new RedirectView(PREFIX + "/");
    }

    @DeleteMapping("/**")
    public RedirectView redirectDeletes() {
        return new RedirectView(PREFIX + "/");
    }

    @GetMapping(PREFIX + "/{provider}/{id}")
    public ResponseContent handleGetRequest(@RequestParam String provider, @RequestParam String id) {
        User user = service.getUser(provider, id);
        return new ResponseContent.Builder().setData(user).build();
    }

    @PutMapping(PREFIX + "/{provider}/{id}")
    public ResponseContent handlePutRequest(@RequestParam String provider, @RequestParam String id) {
        User user = service.updateUser(provider, id);
        return new ResponseContent.Builder().setData(user).build();
    }

    @PostMapping(PREFIX + "/{provider}/{id}")
    public ResponseContent handlePostRequest(@RequestParam String provider, @RequestParam String id) {
        User user = service.addUser(provider, id);
        return new ResponseContent.Builder().setData(user).build();
    }

    @DeleteMapping(PREFIX + "/{provider}/{id}")
    public ResponseContent handleDeleteRequest(@RequestParam String provider, @RequestParam String id) {
        User user = service.deleteUser(provider, id);
        return new ResponseContent.Builder().setData(user).build();
    }
}
