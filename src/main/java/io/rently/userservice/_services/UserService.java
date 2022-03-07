package io.rently.userservice._services;

import io.rently.userservice.dtos.ResponseContent;
import io.rently.userservice.dtos.User;
import io.rently.userservice.errors.enums.Errors;
import io.rently.userservice.interfaces.IDatabaseContext;
import io.rently.userservice.persistency.SqlPersistence;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private static final IDatabaseContext repository;

    // 9aef044d-6549-4785-9234-cb7f9314777a
    // 0269aec5-21cb-4b19-9fe1-90e1d5595dd9
    // 747af12c-6be0-4dfe-8964-f447305d6737
    public static ResponseContent returnUserById(String id) {
        User user;
        try {
            user = repository.getById(User.class, id);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw Errors.INTERNAL_SERVER_ERROR.getException();
        }

        if (user == null) {
            throw Errors.USER_NOT_FOUND.getException();
        }
        
        return new ResponseContent.Builder().setData(user).build();
    }

    public static ResponseContent addUser(User user) {
        repository.add(user.createAsNew());
        return new ResponseContent.Builder().setMessage("Successfully added user with ID { id: " + user.getId() + " }").build();
    }

    public static ResponseContent deleteUserById(String id) {
//        User user = (User) repository.get(User.class.getDeclaredAnnotation(PersistentField.class), id);
//        repository.delete(user);
        return new ResponseContent.Builder().setMessage("Successfully removed user with ID { id: " + id + " }").build();
    }

    public static ResponseContent replaceUserById(String id, User userData) {
//        User user = (User) repository.get();
//        repository.delete(user);
//        repository.add(user.updateInfo(userData));
        return new ResponseContent.Builder().setMessage("Successfully updated user with ID { id: " + id + " }").build();
    }

    private static void handleUniqueProperties(User user, User existingUser) {
        if (Objects.equals(existingUser.getUsername(), user.getUsername()) || Objects.equals(existingUser.getEmail(), user.getEmail())) {
            throw Errors.USER_ALREADY_EXISTS.getException();
        }
    }

    static {
        repository = new SqlPersistence("dbi433816", "admin", "studmysql01.fhict.local", "dbi433816");
    }
}
