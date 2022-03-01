package io.rently.userservice.httpCodes;

public enum HttpCodes {
    OK(200, "OK"),
    CREATE(201, ""),
    NOT_FOUND(404, " not found");

    private int code;
    private String desc;

    private HttpCodes(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
