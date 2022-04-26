package com.carrot.system.util;

import com.google.gson.Gson;

public class StringUtils {
    // 判断字符串为null或者""
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    // 判断字符串不为null或者""
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    // 判断字符串为null或者""或者" "
    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    // 判断字符串不为null或者""或者" "
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    // 判断字符串为null或者""或者" "或者"null"
    public static boolean isBlankOrNull(String str) {
        return str == null || str.trim().length() == 0 || "null".equals(str);
    }

    // 判断字符串不为null或者""或者" "或者"null"
    public static boolean isNotBlankOrNull(String str) {
        return !isBlankOrNull(str);
    }

    // 判断Object为null或者""或者" "或者"null"
    public static boolean isBlankOrNull(Object o) {
        if(o == null) return true;
        if(o instanceof String) {
            return isBlankOrNull((String)o);
        }
        return false;
    }
    public static boolean isNotBlankOrNull(Object o) {
        return !isBlankOrNull(o);
    }


    // 判断string是数字(包括小数)
    public static boolean isNumber(String str){
        if(isBlankOrNull(str)) return false;
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    // 尝试将一个未知类型转为String
    public static String toStringOrJson(Object obj) {
        if(obj == null) return "";
        if (obj instanceof String || obj instanceof Number) {
            return obj.toString();
        }
        return (new Gson()).toJson(obj);
    }

}
