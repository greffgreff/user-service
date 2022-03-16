package io.rently.userservice.util;

import java.sql.Timestamp;

public class Util {

    public static <T> T getNonNull(T this_, T that_) {
        return this_ != null && this_ != "" ? this_ : that_;
    }

    public static Timestamp getCurrentTs() {
        return new Timestamp(System.currentTimeMillis());
    }
}
