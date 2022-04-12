package com.vegetables.core.factory.classPool;

import com.vegetables.system.notch.BeforeReturnFunction;

import java.util.List;
import java.util.logging.Logger;

public class BeforeReturnPool{
    private static final Logger log = Logger.getGlobal();

    private static final ClassPoolCore CLASS_POOL_CORE = new ClassPoolCore();

    public static void trim() {
        for(Class<?> clazz:getClasses()){
            if(clazz.isAssignableFrom(BeforeReturnFunction.class)){
                log.info("移除无效后置拦截器：" + clazz.getName());
                CLASS_POOL_CORE.remove(clazz);
            }
        }
    }

    public static void add(Class<?> clazz) {
        CLASS_POOL_CORE.add(clazz);
    }

    public static List<Class<?>> getClasses() {
        return CLASS_POOL_CORE.getClasses();
    }

    public static void setClasses(List<Class<?>> classes) {
        CLASS_POOL_CORE.setClasses(classes);
    }
}
