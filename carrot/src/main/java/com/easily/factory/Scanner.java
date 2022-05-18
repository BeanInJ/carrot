package com.easily.factory;

import com.easily.core.ConfigCenter;

import java.util.List;

public interface Scanner {
    List<ClassMeta> getAll();
    boolean put(String name,Class<?> clazz);
    boolean put(Class<?> clazz);
    ClassMeta get(String name);
    void unifyInit() throws IllegalAccessException, InstantiationException ;
    void configCenter(ConfigCenter configCenter);
}
