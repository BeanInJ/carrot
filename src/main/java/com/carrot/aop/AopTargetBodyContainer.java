package com.carrot.aop;

import com.carrot.cache.LruCache;

/**
 * 缓存已经加载过的目标方法
 */
public class AopTargetBodyContainer {

    private static LruCache<String, AopBody> targetBodyMap = new LruCache<String, AopBody>(1024);

    public static AopBody get(String key) {
        return targetBodyMap.get(key);
    }

    public static void put(String key, AopBody targetBody) {
        targetBodyMap.put(key, targetBody);
    }
}
