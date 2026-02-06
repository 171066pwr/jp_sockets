package com.mycompany.app.common;

import java.util.Arrays;

public class MessageSplitter {
    public static final String SEPARATOR = ":";

    public static String[] split(String line) {
        return line.split(SEPARATOR);
    }

    public static String combine(Object ... parts) {
        String[] strings = Arrays.stream(parts).map(Object::toString).toArray(String[]::new);
        return String.join(SEPARATOR, strings);
    }
}
