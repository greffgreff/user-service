package io.rently.userservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class ResponseBody {
    @JsonProperty("timestamp")
    private final Timestamp timestamp;

    @JsonProperty("status")
    private final int status;

    @JsonProperty("content")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Object data;

    private ResponseBody(Builder builder) {
        this.timestamp = builder.timestamp;
        this.status = builder.status;
        this.data = builder.data;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public Object getData() {
        return data;
    }

    public static class Builder {
        private final Timestamp timestamp;
        private final int status;
        private Object data;

        public Builder(Timestamp timestamp, int status) {
            this.timestamp = timestamp;
            this.status = status;
        }

        public Builder setData(Object data) {
            this.data = data;
            return this;
        }

        public ResponseBody build() {
            ResponseBody responseBody = new ResponseBody(this);
            validate();
            return responseBody;
        }

        private void validate() {
            // validation
        }
    }
}