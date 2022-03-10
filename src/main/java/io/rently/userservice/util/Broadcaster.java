package io.rently.userservice.util;

public class Broadcaster {
    private static final String PREFIX = "[USER ENDPOINT] ";

    public static void debug(Object obj) {
        System.out.println(PREFIX + Util.getCurrentTs() + " [DEBUG] " + obj);
    }

    public static void info(Object obj) {
        System.out.println(PREFIX + Util.getCurrentTs() + " [INFO] " + obj);
    }

    public static void warn(Object obj) {
        System.out.println(PREFIX + Util.getCurrentTs() + " [WARN] " + obj);
    }

    public static void error(Object obj) {
        System.out.println(PREFIX + Util.getCurrentTs() + " [ERROR] " + obj);
    }

    public static void error(Object msg, Exception ex) {
        System.out.println(PREFIX + Util.getCurrentTs() + " [ERROR] " + msg);
        System.out.println("---------------------------------- Stack trace ----------------------------------");
        ex.printStackTrace();
    }
}
