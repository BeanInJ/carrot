package com.vegetables.core.factory.classPool;

import com.vegetables.system.aop.active.*;

import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Logger;

/**
 * aop 切面类池
 */
public class AopPool {
    private static final Logger log = Logger.getGlobal();

    private static final ClassPoolCore CLASS_POOL_CORE = new ClassPoolCore();

    public static void trim() {
        classFor: for(Class<?> clazz:getClasses()){
            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(AOPAfter.class)){
                    continue classFor;
                }else if(method.isAnnotationPresent(AOPAround.class)){
                    continue classFor;
                }else if(method.isAnnotationPresent(AOPBefore.class)){
                    continue classFor;
                }else if(method.isAnnotationPresent(AOPCatch.class)){
                    continue classFor;
                }else if(method.isAnnotationPresent(AOPFinally.class)){
                    continue classFor;
                }
            }
            log.info("移除无效AOP：" + clazz.getName());
            CLASS_POOL_CORE.remove(clazz);
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
