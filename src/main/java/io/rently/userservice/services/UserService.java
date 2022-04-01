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
        return repository.findByProviderInfo(provider, id);
    }

    public void addUser(User user) {
        repository.saveAndFlush(user);
    }

    public User updateUser(String provider, String id) {
        return null;
    }

    public User deleteUser(String provider, String id) {
        return null;
    }
}
