package com.dayrain.common;

public class StringUtils {

    public static boolean equals(String pre, String cur) {
        if(pre == null) {
            return cur == null;
        }

        return pre.trim().equals(cur);
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}
