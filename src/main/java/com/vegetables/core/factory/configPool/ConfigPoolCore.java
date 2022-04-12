package com.vegetables.core.factory.configPool;

import java.util.HashMap;
import java.util.Map;

public class ConfigPoolCore {
    private final Map<String,Object> configMap = new HashMap<>();

    public void addConfig(String configName,Object value){
        this.configMap.put(configName, value);
    }

    public Object getConfig(String configName){
        return this.configMap.get(configName);
    }

    public void removeConfig(String configName){
        this.configMap.remove(configName);
    }
}
