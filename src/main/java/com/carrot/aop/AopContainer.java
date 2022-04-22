package com.carrot.aop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * aop容器
 */
public class AopContainer {
    private static final Map<String,Class<?>> aopMp = new HashMap<>();

    public static void put(String key,Class<?> clazz){
        aopMp.put(key, clazz);
    }

    public static Class<?> get(String key){
        return aopMp.get(key);
    }

    public static List<Class<?>> getAop(String targetMethodName){
        List<Class<?>> aopList = new ArrayList<>();
        // 循环map
        for (Map.Entry<String, Class<?>> entry : aopMp.entrySet()) {
            // 匹配
            Pattern pattern = Pattern.compile(entry.getKey());
            Matcher matcher = pattern.matcher(targetMethodName);
            if (matcher.matches()){
                aopList.add(entry.getValue());
            }
        }
        return aopList;
    }
}
