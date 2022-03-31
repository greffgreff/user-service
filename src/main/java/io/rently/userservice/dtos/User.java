package io.rently.userservice.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = User.Builder.class)
public class User {
    public final String id;
    public final String providerId;
    public final String provider;
    public final String name;
    public final String email;
    public final String phone;
    public final String createdAt;
    public final String updatedAt;

    public User(Builder builder) {
        this.id = builder.id;
        this.providerId = builder.providerId;
        this.provider = builder.provider;
        this.name = builder.name;
        this.email = builder.email;
        this.phone = builder.phone;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static class Builder {
        @JsonProperty
        public String id;
        @JsonProperty
        public final String providerId;
        @JsonProperty
        public final String provider;
        @JsonProperty
        public String name;
        @JsonProperty
        public String email;
        @JsonProperty
        public String phone;
        @JsonProperty
        public String createdAt;
        @JsonProperty
        public String updatedAt;

        public Builder(String providerId, String provider) {
            this.providerId = providerId;
            this.provider = provider;
        }

        @JsonCreator
        public User build() {
            return new User(this);
        }
    }
}