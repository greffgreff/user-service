package io.rently.userservice.controllers;

import io.rently.userservice.dtos.ResponseContent;
import io.rently.userservice.dtos.User;
import io.rently.userservice.errors.Errors;
import io.rently.userservice.services.UserService;
import io.rently.userservice.utils.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v2/users")
public class UserController {

    @Autowired
    public UserService service;
    @Autowired
    private Jwt jwt;

    @GetMapping("/{id}")
    public ResponseContent handleGetRequest(@PathVariable String id) {
        User user = service.getUserById(id);
        return new ResponseContent.Builder().setData(user).build();
    }

    @GetMapping("/{provider}/{providerId}")
    public ResponseContent handleGetRequest(@PathVariable String provider, @PathVariable String providerId) {
        User user = service.getUserByProvider(provider, providerId);
        return new ResponseContent.Builder().setData(user).build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    public ResponseContent handlePostRequest(@RequestHeader("Authorization") String header, @RequestBody User user) {
        verifyOwnership(header, user.getId());
        service.addUser(user);
        return new ResponseContent.Builder(HttpStatus.CREATED).setMessage("Successfully added user to database").build();
    }

    @PutMapping("/{id}")
    public ResponseContent handlePutRequest(@RequestHeader("Authorization") String header, @PathVariable String id, @RequestBody User user) {
        verifyOwnership(header, user.getId());
        service.updateUser(id, user);
        return new ResponseContent.Builder().setMessage("Successfully updated user from database").build();
    }

    @DeleteMapping("/{id}")
    public ResponseContent handleDeleteRequest(@RequestHeader("Authorization") String header, @PathVariable String id) {
        verifyOwnership(header, id);
        service.deleteUser(id);
        return new ResponseContent.Builder().setMessage("Successfully removed user from database").build();
    }

    protected void verifyOwnership(String bearer, String userId) {
        String token = bearer.split(" ")[1];
        String id = jwt.getClaims(token).getSubject();
        if (!Objects.equals(id, userId)) {
            throw Errors.UNAUTHORIZED_REQUEST;
        }
    }
}
