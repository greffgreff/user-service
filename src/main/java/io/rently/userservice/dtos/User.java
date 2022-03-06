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
    @PersistentField(name = "id")
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

    @PersistentField(name = "phone_number")
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String phone;

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

    public String getPhone() {
        return phone;
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
        phone = Util.getNonNull(phone, userData.getPhone());
        updatedOn = Util.getCurrentTs();
        return this;
    }
}