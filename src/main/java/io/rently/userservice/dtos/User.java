package io.rently.userservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.rently.userservice.annotations.PersistentObjectId;
import io.rently.userservice.annotations.PersistentObject;
import io.rently.userservice.annotations.PersistentField;
import io.rently.userservice.util.Util;

import java.sql.Timestamp;
import java.util.UUID;

@PersistentObject(name = "users")
@JsonDeserialize
public class User {
    @PersistentObjectId
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
    private Timestamp createdOn;

    @PersistentField(name = "last_update")
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp updatedOn;

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

    public User createAsNew() {
        id = UUID.randomUUID().toString();
        createdOn = Util.getCurrentTs();
        updatedOn = Util.getCurrentTs();
        return this;
    }

    public User updateInfo(User userData) {
        username = Util.getNonNull(username, userData.getUsername());
        fullName = Util.getNonNull(fullName, userData.getFullName());
        email = Util.getNonNull(email, userData.getEmail());
        gender = Util.getNonNull(gender, userData.getGender());
        phone = Util.getNonNull(phone, userData.getPhone());
        password = Util.getNonNull(password, userData.getPassword());
        salt = Util.getNonNull(salt, userData.getSalt());
        updatedOn = Util.getCurrentTs();
        return this;
    }
}