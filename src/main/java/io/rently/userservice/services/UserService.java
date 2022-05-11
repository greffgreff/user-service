package io.rently.userservice.services;

import io.jsonwebtoken.SignatureAlgorithm;
import io.rently.userservice.dtos.User;
import io.rently.userservice.errors.Errors;
import io.rently.userservice.interfaces.UserRepository;
import io.rently.userservice.utils.Jwt;
import io.rently.userservice.utils.Broadcaster;
import io.rently.userservice.utils.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private Jwt jwt;
    @Autowired
    private MailerService mailer;

    public User getUserByProvider(String provider, String providerId) {
        Broadcaster.info("Fetching user from database by provider: " + provider + "/" + providerId);
        return tryFindUserByProvider(provider, providerId);
    }

    public User getUserById(String id) {
        Broadcaster.info("Fetching user from database by id: " + id);
        return tryFindUserById(id);
    }

    public void addUser(User user) {
        Broadcaster.info("Adding user to database: " + user.getId());
        validateData(user);
        Optional<User> existingUser = repository.findByProviderAndProviderId(user.getProvider(), user.getProviderId());
        Optional<User> existingUserById = repository.findById(user.getId());
        if (existingUser.isPresent() || existingUserById.isPresent()) {
            throw Errors.USER_ALREADY_EXISTS;
        }
        repository.save(user);
        try {
            mailer.dispatchGreeting(user.getName(), user.getEmail());
        } catch (Exception exception) {
            Broadcaster.warn("Could not dispatch greetings to " + user.getEmail());
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
        repository.save(user);
    }

    public void deleteUser(String id) {
        Broadcaster.info("Removing user from database: " + id);
        User user = tryFindUserById(id);
        repository.deleteById(id);
        try {
            mailer.dispatchGoodbye(user.getName(), user.getEmail());
        } catch (Exception exception) {
            Broadcaster.warn("Could not dispatch goodbyes to " + user.getEmail());
            Broadcaster.error(exception);
        }
    }

    public User tryFindUserByProvider(String provider, String providerId) {
        Optional<User> user = repository.findByProviderAndProviderId(provider, providerId);
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

    public void verifyOwnership(String token, String userId) {
        User user = tryFindUserById(userId);
        String id = null;
        try {
            id = jwt.getParser().parseClaimsJws(token).getBody().getSubject();
        } catch (Exception ingore) {
            throw Errors.UNAUTHORIZED_REQUEST;
        }
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
        } else if (user.getEmail() == null) {
            throw new Errors.HttpFieldMissing("email");
        } else if (user.getName() == null) {
            user.setName(user.getEmail());
        } else if(user.getCreatedAt() == null) {
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()).toString());
        } else if (!Validation.canParseToTs(user.getCreatedAt())) {
            throw new Errors.HttpValidationFailure("createdAt", Timestamp.class, user.getCreatedAt());
        } else if (user.getUpdatedAt() == null) {
            user.setUpdatedAt(new Timestamp(System.currentTimeMillis()).toString());
        } else if (!Validation.canParseToTs(user.getUpdatedAt())) {
            throw new Errors.HttpValidationFailure("updatedAt", Timestamp.class, user.getUpdatedAt());
        }
    }
}
