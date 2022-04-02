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

    public User getUserByProvider(String provider, String providerId) {
        Broadcaster.info("Fetching user from database by provider: " + provider + " " + providerId);
        return tryFindUserByProvider(provider, providerId);
    }

    public User getUserById(String id) {
        Broadcaster.info("Fetching user from database by id: " + id);
        return tryFindUserById(id);
    }

    public void addUser(User user) {
        Broadcaster.info("Adding user to database: " + user.getProvider() + " " + user.getProviderId());
        validateData(user);
        repository.saveAndFlush(user);
    }

    public void updateUser(String id, User user) {
        Broadcaster.info("Updating user from database by id: " + id);
        tryFindUserById(id);
        validateData(user);
        deleteUser(id);
        addUser(user);
    }

    public void deleteUser(String id) {
        Broadcaster.info("Removing user from database by id: " + id);
        tryFindUserById(id);
        repository.deleteById(id);
    }

    public User tryFindUserByProvider(String provider, String providerId) {
        User user = repository.findByProviderInfo(provider, providerId).get();
        if (user == null) {
            throw Errors.USER_NOT_FOUND;
        }
        return user;
    }

    public User tryFindUserById(String id) {
        User user = repository.findById(id).get();
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
