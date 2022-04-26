package com.easily.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LRU算法：当缓存数超出规定数量时，最近一次使用时间离现在时间最远的数据将被删除掉
 */
public class LruCache<T,V> extends LinkedHashMap<T,V> {
    private static final long serialVersionUID = 1L;

    // capacity 指定容量
    private final int capacity;

    /**
     * 临界值（threshold） = 负载因子（loadFactor） * 容量（capacity）
     */
    public LruCache(int capacity) {
        // capacity 容量 ，0.75F 负载因子, true 访问操作
        super(capacity,0.75F,true);
        this.capacity = capacity;
    }

    /**
     * 如果map里面的元素个数大于了缓存最大容量，则删除 “链表的顶端元素”
     * 也就是删除 “最近一次使用时间离现在时间最远” 的数据
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<T, V> eldest) {
        return size() > capacity;
    }
}
