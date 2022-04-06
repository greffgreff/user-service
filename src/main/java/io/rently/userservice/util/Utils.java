package io.rently.userservice.util;

import java.sql.Timestamp;

public class Utils {

    public static Timestamp getCurrentTs() {
        return new Timestamp(System.currentTimeMillis());
    }
}
