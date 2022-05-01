package com.easily.cache;

import com.easily.system.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Session 缓存
 * 1、超时后清理(24小时)
 * 2、可被redis或其他缓存替换
 */
public class Session {
    // 保存结构 {uuid，[data,time]}
    private final ConcurrentMap<String, Object[]> map = new ConcurrentHashMap<>();
    private final static long MAX_TIME = 1000 * 60 * 60 * 24;

    /**
     * 保存session
     */
    public String put(Object object){
        // 判断object是否为空
        if(StringUtils.isBlankOrNull(object)) return null;
        String key = UUID.randomUUID().toString().trim().replaceAll("-", "");
        long time = System.currentTimeMillis();
        Object[] objects = new Object[]{object, time};
        return map.put(key, objects) == null ? key : null;
    }

    /**
     * 获取session
     */
    public Object get(String key){
        Object[] objects = map.get(key);
        if(objects == null) return null;
        // 超过24小时则清理
        boolean isExpire = System.currentTimeMillis() - (long)objects[1] > MAX_TIME;
        if(isExpire){
            map.remove(key);
            return null;
        }
        return objects[0];
    }

    public <T> T get(String key, Class<T> expectedType){
        Object object = get(key);
        if(object == null) return null;
        return expectedType.cast(object);
    }
}
