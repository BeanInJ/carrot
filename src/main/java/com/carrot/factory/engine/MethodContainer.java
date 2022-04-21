package com.carrot.factory.engine;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 方法体容器
 *
 * 本容器里资源开放范围：全局可获取
 */
public class MethodContainer {
    /**
     * 保存已生成的方法体 {id，方法体}
     */
    private static final Map<String,MethodBody> methodBodyMap = new HashMap<>();

    protected static void put(String id,MethodBody methodBody){
        methodBodyMap.put(id,methodBody);
    }

    public static MethodBody get(String id){
        return methodBodyMap.get(id);
    }

    public static Method getMethod(String id){
        return methodBodyMap.get(id).getMethod();
    }
}
