package io.rently.userservice.services;

import io.rently.userservice.dtos.User;
import io.rently.userservice.errors.Errors;
import io.rently.userservice.errors.Errors;
import io.rently.userservice.interfaces.UserRepository;
import io.rently.userservice.util.Broadcaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public User getUser(String provider, String id) {
        Broadcaster.info("Fetching user from database: " + provider + " " + id);
        User user = repository.findByProviderInfo(provider, id);
        if (user == null) {
            throw Errors.USER_NOT_FOUND;
        }
        return user;
    }

    public void addUser(User user) {
        if (user == null) {
            throw Errors.NO_DATA;
        }
        Broadcaster.info("Adding user to database: " + user.getProvider() + " " + user.getProviderId());
        repository.saveAndFlush(user);
    }

    public User updateUser(String provider, String id, User user) {
        if (user == null) {
            throw Errors.NO_DATA;
        }
        if (repository.findByProviderInfo(provider, id) == null) {
            throw Errors.USER_NOT_FOUND;
        }
        Broadcaster.info("Updating user from database: " + provider + " " + id);
        deleteUser(provider, id);
        addUser(user);
        return null;
    }

    public void deleteUser(String provider, String id) {
        Broadcaster.info("Removing user from database: " + provider + " " + id);
        repository.deleteByProviderInfo(provider, id);
    }
}
