package com.carrot.system.BaseServer.pool;

import com.carrot.aop.annotation.*;
import com.carrot.core.step.factory.AddPool;
import com.carrot.core.step.factory.ClassPoolCore;
import com.carrot.core.step.factory.Pool;
import com.carrot.system.BaseServer.pool.label.Aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Logger;

/**
 * aop 切面类池
 */
@AddPool
public class AopPool implements Pool {
    private static final Logger log = Logger.getGlobal();

    private static final ClassPoolCore CLASS_POOL_CORE = new ClassPoolCore();

    public void trim() {
        classFor: for(Class<?> clazz:getClasses()){
            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(AopAfter.class)){
                    continue classFor;
                }else if(method.isAnnotationPresent(AopAround.class)){
                    continue classFor;
                }else if(method.isAnnotationPresent(AopBefore.class)){
                    continue classFor;
                }else if(method.isAnnotationPresent(AopException.class)){
                    continue classFor;
                }else if(method.isAnnotationPresent(AopFinally.class)){
                    continue classFor;
                }
            }
            log.info("移除无效AOP：" + clazz.getName());
            CLASS_POOL_CORE.remove(clazz);
        }
    }

    @Override
    public Class<? extends Annotation> getLabel() {
        return Aop.class;
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

    @Override
    public void add(Object o) {
        CLASS_POOL_CORE.add((Class<?>)o);
    }
}
