package io.rently.userservice.util;

public class Broadcaster {
    private static final String PREFIX = "[ USER ENDPOINT ] ";

    public static void debug(Object obj) {
        System.out.println(PREFIX + " [DEBUG] " + obj);
    }

    public static void info(Object obj) {
        System.out.println(PREFIX + " [INFO] " + obj);
    }

    public static void warn(Object obj) {
        System.out.println(PREFIX + " [WARN] " + obj);
    }

    public static void error(Object obj) {
        System.out.println(PREFIX + " [ERROR] " + obj);
    }
}
