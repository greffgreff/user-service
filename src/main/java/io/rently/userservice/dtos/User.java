package io.rently.userservice.dtos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "users")
public class User {
    // both `providerId` (user id from provider)
    // and `provider` can act as composite primary keys
    // and likely won't be changing.
    // `id` key added if this is not the case.
    @Id
    @Column(updatable = false, nullable = false, unique = true)
    private String id;
    @Column(updatable = false, nullable = false, columnDefinition = "TEXT")
    private String providerId;
    @Column(updatable = false, nullable = false, columnDefinition = "TEXT")
    private String provider;
    @Column(columnDefinition = "TEXT")
    private String name;
    @Column(columnDefinition = "TEXT")
    private String email;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String createdAt;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String updatedAt;

    protected User() { }

    public String getId() {
        return id;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getProvider() {
        return provider;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", providerId='" + providerId + '\'' +
                ", provider='" + provider + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}