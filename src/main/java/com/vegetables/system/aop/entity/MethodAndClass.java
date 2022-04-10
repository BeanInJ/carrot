package com.vegetables.system.aop.entity;

import java.lang.reflect.Method;

/**
 * 一个方法对应一个类
 */
public class MethodAndClass {
    /**
     * aop切入时要执行的方法
     */
    private Method method;
    /**
     * aop切入时要执行的方法所在的类
     */
    private Object object;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
