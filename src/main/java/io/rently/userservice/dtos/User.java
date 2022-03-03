package io.rently.userservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.rently.userservice.annotations.SqlEntity;
import io.rently.userservice.annotations.SqlField;
import io.rently.userservice.util.Util;
import org.springframework.lang.NonNull;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.HashMap;

@SqlEntity(tableName = "users")
@JsonDeserialize
public class User {
    @SqlField(columnName = "id")
    private String id;

    @SqlField(columnName = "username")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String username;

    @SqlField(columnName = "full_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fullName;

    @SqlField(columnName = "email")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;

    @SqlField(columnName = "phone_number")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String phone;

    @SqlField(columnName = "creation_date")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp createdOn;

    @SqlField(columnName = "last_update")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp updatedOn;

    public User(String id) {
        this.id = id;
    }

    public User(String id, String username, String fullName, String email, String phone, Timestamp createdOn, Timestamp updatedOn) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
    }

    @JsonProperty
    public User setId(@NonNull String id) {
        this.id = id;
        return this;
    }

    @JsonProperty
    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    @JsonProperty
    public User setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    @JsonProperty
    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    @JsonProperty
    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    @JsonProperty
    public User setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    @JsonProperty
    public User setUpdatedOn(Timestamp updatedOn) {
        this.updatedOn = updatedOn;
        return this;
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

    public String getPhone() {
        return phone;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public Timestamp getUpdatedOn() {
        return updatedOn;
    }

    public User refreshCreationDate() {
        this.createdOn = Util.getCurrentTs();
        return this;
    }

    public User refreshUpdateDate() {
        this.updatedOn = Util.getCurrentTs();
        return this;
    }
}
