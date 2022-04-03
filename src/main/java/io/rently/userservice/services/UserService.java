package io.rently.userservice.services;

import io.rently.userservice.dtos.User;
import io.rently.userservice.errors.Errors;
import io.rently.userservice.errors.Errors;
import io.rently.userservice.interfaces.UserRepository;
import io.rently.userservice.util.Broadcaster;
import io.rently.userservice.util.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

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
        Broadcaster.info("Adding user to database: " + user.getId());
        validateData(user);
        repository.saveAndFlush(user);
    }

    public void updateUser(String id, User user) {
        Broadcaster.info("Updating user from database: " + id);
        tryFindUserById(id);
        validateData(user);
        deleteUser(id);
        addUser(user);
    }

    public void deleteUser(String id) {
        Broadcaster.info("Removing user from database: " + id);
        tryFindUserById(id);
        repository.deleteById(id);
    }

    public User tryFindUserByProvider(String provider, String providerId) {
        Optional<User> user = repository.findByProviderInfo(provider, providerId);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw Errors.USER_NOT_FOUND;
        }
    }

    public User tryFindUserById(String id) {
        Optional<User> user = repository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw Errors.USER_NOT_FOUND;
        }
    }

    public void validateData(User user) {
        if (user == null) {
            throw Errors.NO_DATA;
        } else if (user.getId() == null) {
            throw new Errors.HttpFieldMissing("id");
        } else if (Validation.tryParseUUID(user.getId()) == null) {
            throw new Errors.HttpValidationFailure("id", UUID.class, user.getId());
        } else if (user.getProvider() == null) {
            throw new Errors.HttpFieldMissing("provider");
        } else if (user.getProviderId() == null) {
            throw new Errors.HttpFieldMissing("providerId");
        }
    }
}
