package com.easily.factory;

import java.util.Map;

public interface Scanner {
    Map<String, Class<?>> getAll();
    void put(String name,Class<?> clazz);
    void put(Class<?> clazz);
    Class<?> get(String name);
}
