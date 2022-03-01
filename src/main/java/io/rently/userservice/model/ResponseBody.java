package io.rently.userservice.model;

import java.sql.Timestamp;

public class ResponseBody {
    private final Timestamp timestamp;
    private final int status;
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

    @Override
    public String toString() {
        return "";
    }

    public static class Builder {
        private final Timestamp timestamp; // required
        private final int status; // required
        private Object data; // optional

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