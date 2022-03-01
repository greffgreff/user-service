package io.rently.userservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    @JsonProperty("id")
    private final String id; // required

    @JsonProperty("username")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String username;

    @JsonProperty("fullname")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String fullname;

    @JsonProperty("email")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String email;

    @JsonProperty("phone")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String phone;

    private User(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.fullname = builder.fullname;
        this.email = builder.email;
        this.phone = builder.phone;
    }

    public static class Builder {
        private final String id;
        private String username;
        private String fullname;
        private String email;
        private String phone;

        public Builder(String id) {
            this.id = id;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setFullname(String fullname) {
            this.fullname = fullname;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public User build() {
            User user = new User(this);
            validate();
            return user;
        }

        private void validate() {
            // validate
        }
    }
}
