package com.easily.factory;

import com.easily.system.common.Getter;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Pools implements Getter<Class<? extends Annotation>,Pool> {
    private static final Logger log = Logger.getGlobal();
    private final Map<Class<? extends Annotation>, Pool> poolMap = new HashMap<>();

    public void add(Pool pool){
        Class<? extends Annotation> label = pool.getLabel();
        if (poolMap.containsKey(label)) {
            log.warning("已跳过重复类池ID: " + pool.getClass().getName());
            return;
        }
        poolMap.put(label,pool);
    }

    public void add(Object o){
        add((Pool) o);
    }

    public boolean containsKey(Class<? extends Annotation> label){
        return poolMap.containsKey(label);
    }

    @Override
    public Pool get(Class<? extends Annotation> label) {
        return poolMap.get(label);
    }

    public Pool get(Pool pool) {
        return poolMap.get(pool.getLabel());
    }

    public Map<Class<? extends Annotation>, Pool> getAll(){
        return poolMap;
    }

    public void end(){
        poolMap.values().forEach(Pool::end);
    }
}
