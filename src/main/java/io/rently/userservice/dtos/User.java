package io.rently.userservice.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.rently.userservice.annotations.PersistentField;
import io.rently.userservice.annotations.PersistentKeyField;
import io.rently.userservice.annotations.PersistentObject;
import io.rently.userservice.util.Broadcaster;
import io.rently.userservice.util.Util;

import java.sql.Timestamp;
import java.util.UUID;

@PersistentObject(name = "_rently_users")
@JsonDeserialize(builder = User.Builder.class)
public class User {
    @PersistentField(name = "id")
    @PersistentKeyField(name = "id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;

    @PersistentField(name = "username")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String username;

    @PersistentField(name = "full_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fullName;

    @PersistentField(name = "email")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;

    @PersistentField(name = "gender")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String gender;

    @PersistentField(name = "phone_number")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String phone;

    @PersistentField(name = "password")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    @PersistentField(name = "salt")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String salt;

    @PersistentField(name = "creation_date")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String createdOn;

    @PersistentField(name = "last_update")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String updatedOn;

    public User() { }

    public User(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.fullName = builder.fullName;
        this.email = builder.email;
        this.gender = builder.gender;
        this.phone = builder.phone;
        this.password = builder.password;
        this.salt = builder.salt;
        this.createdOn = builder.createdOn;
        this.updatedOn = builder.updatedOn;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public User createAsNew() {
        id = UUID.randomUUID().toString();
        createdOn = new Timestamp(System.currentTimeMillis()).toString();
        updatedOn = new Timestamp(System.currentTimeMillis()).toString();
        return this;
    }

    public User updateInfo(User userData) {
        username = Util.getNonNull(userData.getUsername(), username);
        fullName = Util.getNonNull(userData.getFullName(), fullName);
        email = Util.getNonNull(userData.getEmail(), email);
        gender = Util.getNonNull(userData.getGender(), gender);
        phone = Util.getNonNull(userData.getPhone(), phone);
        password = Util.getNonNull(userData.getPassword(), password);
        salt = Util.getNonNull(userData.getSalt(), salt);
        createdOn = Util.getNonNull(userData.getCreatedOn(), createdOn);
        updatedOn = new Timestamp(System.currentTimeMillis()).toString();
        return this;
    }

    public static class Builder {
        public String id;
        public String username;
        public String fullName;
        public String email;
        public String gender;
        public String phone;
        public String password;
        public String salt;
        public String createdOn;
        public String updatedOn;

        @JsonProperty
        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        @JsonProperty
        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        @JsonProperty
        public Builder setFullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        @JsonProperty
        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        @JsonProperty
        public Builder setGender(String gender) {
            this.gender = gender;
            return this;
        }

        @JsonProperty
        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        @JsonProperty
        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        @JsonProperty
        public Builder setSalt(String salt) {
            this.salt = salt;
            return this;
        }

        @JsonProperty
        public Builder setCreatedOn(String createdOn) {
            this.createdOn = createdOn;
            return this;
        }

        @JsonProperty
        public Builder setUpdatedOn(String updatedOn) {
            this.updatedOn = updatedOn;
            return this;
        }

        @JsonCreator
        public User build() {
            return new User(this);
        }
    }
}