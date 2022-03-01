package io.rently.userservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    @JsonProperty("username")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String username;

    @JsonProperty("fullname")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fullname;

    @JsonProperty("email")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;

    @JsonProperty("phone")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String phone;
}
