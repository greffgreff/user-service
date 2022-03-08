package io.rently.userservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.rently.userservice.annotations.PersistentField;
import io.rently.userservice.annotations.PersistentKeyField;
import io.rently.userservice.annotations.PersistentObject;
import io.rently.userservice.util.Broadcaster;
import io.rently.userservice.util.Util;

import java.sql.Timestamp;
import java.util.UUID;

@PersistentObject(name = "_rently_users")
@JsonDeserialize
public class User {
    @PersistentField(name = "id")
    @PersistentKeyField(name = "id")
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;

    @PersistentField(name = "username")
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String username;

    @PersistentField(name = "full_name")
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fullName;

    @PersistentField(name = "email")
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;

    @PersistentField(name = "gender")
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String gender;

    @PersistentField(name = "phone_number")
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String phone;

    @PersistentField(name = "password")
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    @PersistentField(name = "salt")
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String salt;

    @PersistentField(name = "creation_date")
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String createdOn;

    @PersistentField(name = "last_update")
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String updatedOn;

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
}