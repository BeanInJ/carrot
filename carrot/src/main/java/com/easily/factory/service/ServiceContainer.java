package com.easily.factory.service;

import com.easily.factory.ProductContainer;
import com.easily.system.common.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ServiceContainer implements ProductContainer, Getter<String,Object> {
    private static final Logger log = Logger.getGlobal();
    private final Map<String, Object> map = new HashMap<>();

    @Override
    public Object get(String key) {
        return map.get(key);
    }

    public void put(Object o){
        map.put(o.getClass().getName(),o);
    }

    public void put(String name,Object o){
        map.put(name,o);
    }
}
