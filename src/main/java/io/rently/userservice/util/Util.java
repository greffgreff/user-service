package io.rently.userservice.util;

public class Util {

    public static <T> T getNonNull(T this_, T that_) {
        return this_ != null ? this_ : that_;
    }
}
