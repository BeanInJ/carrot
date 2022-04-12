package com.vegetables.util;

public class UrlUtils {
    /**
     * 纠正URL
     */
    public static String correctUrl(String url) {
        if (url.startsWith("http://")) {
            return url;
        } else {
            return "http://" + url;
        }
    }

    /**
     * 纠正URI
     */
    public static String correctUri(String url) {
        if (!url.startsWith("/")) {
            url = "/" + url;
        }

        if (url.endsWith("/")) {
            if (url.length() != 1) {
                url = url.substring(0, url.length() - 1);
            }
        }
        return url;
    }
}
