package io.rently.userservice.services;

import io.rently.userservice.dtos.User;
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
        return repository.findByProviderInfo(provider, id);
    }

    public void addUser(User user) {
        Broadcaster.info("Adding user to database: " + user.getProvider() + " " + user.getProviderId());
        repository.saveAndFlush(user);
    }

    public User updateUser(String provider, String id, User user) {
        Broadcaster.info("Updating user from database: " + provider + " " + id);
        return null;
    }

    public void deleteUser(String provider, String id) {
        Broadcaster.info("Removing user from database: " + provider + " " + id);
        repository.deleteByProviderInfo(provider, id);
    }
}
