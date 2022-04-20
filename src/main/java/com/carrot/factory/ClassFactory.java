package com.carrot.factory;

import com.carrot.core.step.factory.Pool;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ClassFactory {
    private static final Logger log = Logger.getGlobal();

    /**
     * ｛”类池名“，类池｝
     */
    private static final Map<String,Pool> pools = new HashMap<>();

    /**
     * 获取类池
     */
    public static Pool getPool(String poolName){
        return pools.get(poolName);
    }

    /**
     * 添加类池
     */
    protected static void putPool(String poolName,Pool pool){
        pools.put(poolName,pool);
    }
}
