package com.vegetables.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUtils {
    // 复制map    参数：源map， 返回新map
    public static Map<String, String> copyMap(Map<String, String> sourceMap) {
        Map<String, String> targetMap = new HashMap<String, String>();
        for (String key : sourceMap.keySet()) {
            targetMap.put(key, sourceMap.get(key));
        }
        return targetMap;
    }
}
