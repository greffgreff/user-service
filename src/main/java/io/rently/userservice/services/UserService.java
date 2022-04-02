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
        return tryFindUser(provider, id);
    }

    public void addUser(User user) {
        Broadcaster.info("Adding user to database: " + user.getProvider() + " " + user.getProviderId());
        validateData(user);
        repository.saveAndFlush(user);
    }

    public User updateUser(String provider, String id, User user) {
        Broadcaster.info("Updating user from database: " + provider + " " + id);
        tryFindUser(provider, id);
        validateData(user);
        deleteUser(provider, id);
        addUser(user);
        return null;
    }

    public void deleteUser(String provider, String id) {
        Broadcaster.info("Removing user from database: " + provider + " " + id);
        tryFindUser(provider, id);
        repository.deleteByProviderInfo(provider, id);
    }

    public User tryFindUser(String provider, String providerId) {
        User user = repository.findByProviderInfo(provider, providerId);
        if (user == null) {
            throw Errors.USER_NOT_FOUND;
        }
        return user;
    }

    public void validateData(User user) {
        if (user == null) {
            throw Errors.NO_DATA;
        }
        if (user.getProvider() == null) {
            throw new Errors.HttpFieldMissing("provider");
        }
        if (user.getProviderId() == null) {
            throw new Errors.HttpFieldMissing("providerId");
        }
    }
}
