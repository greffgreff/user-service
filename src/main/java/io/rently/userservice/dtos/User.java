package io.rently.userservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.rently.userservice.persistency.annotations.SqlEntity;
import io.rently.userservice.persistency.annotations.SqlField;
import io.rently.userservice.util.Util;

import java.sql.Timestamp;
import java.util.UUID;

@SqlEntity(tableName = "users")
@JsonDeserialize
public class User {
    @SqlField(columnName = "id")
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;

    @SqlField(columnName = "username")
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String username;

    @SqlField(columnName = "full_name")
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fullName;

    @SqlField(columnName = "email")
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;

    @SqlField(columnName = "phone_number")
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String phone;

    @SqlField(columnName = "creation_date")
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp createdOn;

    @SqlField(columnName = "last_update")
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

    /// testing only
    public User setUsername(String username) {
        this.username = username;
        return this;
    }
    public User setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }
    public User setEmail(String email) {
        this.email = email;
        return this;
    }
    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }
    ///

    public User createAsNew() {
        id = UUID.randomUUID().toString();
        createdOn = Util.getCurrentTs();
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