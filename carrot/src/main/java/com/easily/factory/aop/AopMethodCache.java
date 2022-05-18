package com.easily.factory.aop;

import com.easily.cache.LruCache;

/**
 * 缓存 AopMethod 方法
 */
public class AopMethodCache {
    private static final LruCache<String, AopMethod> targetBodyMap = new LruCache<>(1024);

    public static AopMethod get(String key) {
        return targetBodyMap.get(key);
    }

    public static void put(String key, AopMethod aopMethod) {
        targetBodyMap.put(key, aopMethod);
    }
}
