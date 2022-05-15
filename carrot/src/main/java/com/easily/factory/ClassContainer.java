package com.easily.factory;

import com.easily.system.common.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * class容器
 */
public class ClassContainer implements Getter<String,Class<?>> {
    /**
     * {name,class}
     */
    private final Map<String, Class<?>> classMap = new ConcurrentHashMap<>();

    /**
     * 获取单个
     */
    @Override
    public Class<?> get(String name) {
        return classMap.get(name);
    }

    /**
     * 放入单个
     */
    public void put(String name,Class<?> clazz){
        if(classMap.containsKey(name)){
            throw new RuntimeException("重复class名：" + name);
        }
        classMap.put(name,clazz);
    }

    /**
     * 放入单个
     */
    public void put(Class<?> clazz){
        put(clazz.getName(),clazz);
    }

    public Map<String, Class<?>> getAll(){
        return this.classMap;
    }
}
