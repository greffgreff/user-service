package io.rently.userservice.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.rently.userservice.errors.Errors;
import io.rently.userservice.util.Validation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.UUID;

@Entity(name = "users")
@JsonDeserialize(builder = User.Builder.class)
public class User {
    // both `providerId` (user id from provider)
    // and `provider` can act as composite primary keys
    // and won't be changing for users.
    // `id` key added for the sake of convenience with JPA
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

    public User(Builder builder) {
        this.id = builder.id;
        this.providerId = builder.providerId;
        this.provider = builder.provider;
        this.name = builder.name;
        this.email = builder.email;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

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

    public static class Builder {
        @JsonProperty
        public final String id;
        @JsonProperty
        public final String providerId;
        @JsonProperty
        public final String provider;
        @JsonProperty
        public String name;
        @JsonProperty
        public String email;
        @JsonProperty
        public String createdAt;
        @JsonProperty
        public String updatedAt;

        public Builder(String providerId, String provider) {
            this.id = UUID.randomUUID().toString();
            this.providerId = providerId;
            this.provider = provider;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        @JsonCreator
        public User build() {
            validate();
            return new User(this);
        }

        private void validate() {
            if (provider == null) {
                throw new Errors.HttpFieldMissing("provider");
            }
            if (providerId == null) {
                throw new Errors.HttpFieldMissing("providerId");
            }
        }
    }
}