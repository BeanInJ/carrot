package com.easily.core.bootstrap;

import java.util.HashMap;
import java.util.Map;

/**
 * 仅本包或子类可访问
 */
public class ElementsSingleton {
    private static final Map<Class<? extends ElementsSingleton>, ElementsSingleton> map = new HashMap<>();

    protected ElementsSingleton() {}

    synchronized static  <E extends ElementsSingleton> E get(Class<E> instanceClass){
        if (map.containsKey(instanceClass)) {
            return instanceClass.cast(map.get(instanceClass));
        } else {
            E instance;
            try {
                instance = instanceClass.newInstance();
            }catch (Exception e){
                e.printStackTrace();
                throw new NullPointerException();
            }
            map.put(instanceClass, instance);
            return instance;
        }
    }

    static  <E extends ElementsSingleton> E getOnly(Class<E> instanceClass){
        if (map.containsKey(instanceClass)) {
            return instanceClass.cast(map.get(instanceClass));
        } else {
            E instance;
            try {
                instance = instanceClass.newInstance();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
            map.put(instanceClass, instance);
            return instance;
        }
    }
}
