package io.rently.userservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

import java.sql.Timestamp;

public class ResponseContent {
    @JsonProperty
    @NonNull
    private final Timestamp timestamp;

    @JsonProperty
    @NonNull
    private final int status;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String message;

    @JsonProperty("content")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Object data;

    private ResponseContent(Builder builder) {
        this.timestamp = builder.timestamp;
        this.status = builder.status;
        this.message = builder.message;
        this.data = builder.data;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() { return message; }

    public Object getData() {
        return data;
    }

    public static class Builder {
        private final Timestamp timestamp;
        private final int status;
        private String message;
        private Object data;

        public Builder(Timestamp timestamp, int status) {
            this.timestamp = timestamp;
            this.status = status;
        }

        public Builder(HttpStatus status) {
            this(new Timestamp(System.currentTimeMillis()), status.value());
        }

        public Builder() {
            this(new Timestamp(System.currentTimeMillis()), HttpStatus.OK.value());
        }

        public Builder setData(Object data) {
            this.data = data;
            return this;
        }

        public Builder setMessage(String msg) {
            this.message = msg;
            return this;
        }

        public ResponseContent build() {
            return new ResponseContent(this);
        }
    }
}