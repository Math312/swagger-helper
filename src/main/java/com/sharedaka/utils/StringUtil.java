package com.sharedaka.utils;

public class StringUtil {

    public static String removeSpace(String source) {
        if (source == null) {
            throw new IllegalArgumentException();
        }
        if (source.length() == 0) {
            return source;
        }
        return source.replaceAll("\\s+", "");
    }

    public static String removeHeadAndTailQuotationMarks(String source) {
        String result = source;
        if (result.startsWith("\"")) {
            result = result.substring(1);
        }
        if (result.endsWith("\"")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}
