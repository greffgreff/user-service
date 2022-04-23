package io.rently.userservice.services;

import io.rently.userservice.dtos.User;
import io.rently.userservice.errors.Errors;
import io.rently.userservice.interfaces.UserRepository;
import io.rently.userservice.utils.Jwt;
import io.rently.userservice.utils.Broadcaster;
import io.rently.userservice.utils.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
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
        Optional<User> existingUser = repository.findByProviderInfo(user.getProvider(), user.getProviderId());
        if (existingUser.isPresent()) {
            throw Errors.USER_ALREADY_EXISTS;
        }
        validateData(user);
        repository.save(user);
        try {
            MailerService.dispatchGreeting(user.getName(), user.getEmail());
        } catch (Exception exception) {
            Broadcaster.warn("Greetings not dispatched to " + user.getEmail());
            Broadcaster.error(exception);
        }
    }

    public void updateUser(String id, User user) {
        Broadcaster.info("Updating user from database: " + id);
        if (!Objects.equals(id, user.getId())) {
            throw Errors.INVALID_REQUEST;
        }
        validateData(user);
        tryFindUserById(id);
        repository.deleteById(id);
        repository.save(user);
    }

    public void deleteUser(String id) {
        Broadcaster.info("Removing user from database: " + id);
        User user = tryFindUserById(id);
        repository.deleteById(id);
        MailerService.dispatchGoodbye(user.getName(), user.getEmail());
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

    public void verifyOwnership(String header, String userId) {
        User user = tryFindUserById(userId);
        String id = Jwt.getClaims(header).getSubject();

        if (!Objects.equals(id, user.getId())) {
            throw Errors.UNAUTHORIZED_REQUEST;
        }
    }

    public static void verifyOwnership(String header, User user) {
        String id = Jwt.getClaims(header).getSubject();

        if (!Objects.equals(id, user.getId())) {
            throw Errors.UNAUTHORIZED_REQUEST;
        }
    }

    public static void validateData(User user) {
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
