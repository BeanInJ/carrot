package com.carrot.factory.pool;

import com.carrot.aop.annotation.*;
import com.carrot.factory.ClassPool;
import com.carrot.factory.PoolActuator;
import com.carrot.factory.label.Aop;
import com.carrot.system.dict.INNER;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * 切面类池
 */
public class AopPool extends ClassPool {
    private static final Logger log = Logger.getGlobal();

    /**
     * 清除类列表中，不包含aop方法的类
     */
    @Override
    public void trim() {
        Iterator<Class<?>> clazzIterator = this.classes.iterator();
        out:
        while (clazzIterator.hasNext()) {
            Class<?> clazz = clazzIterator.next();

            // 循环方法，检查注解
            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(AopAfter.class)
                        || method.isAnnotationPresent(AopAround.class)
                        || method.isAnnotationPresent(AopBefore.class)
                        || method.isAnnotationPresent(AopException.class)
                        || method.isAnnotationPresent(AopFinally.class)) {
                    continue out;
                }
            }
            log.info("移除无效AOP：" + clazz.getName());
            this.classes.remove(clazz);
        }
    }

    @Override
    public Class<? extends Annotation> getLabel() {
        return Aop.class;
    }

    @Override
    public String getPoolName() {
        return INNER.AOP_POOl_NAME;
    }

    @Override
    public PoolActuator getActuator() {
        return null;
    }


}
