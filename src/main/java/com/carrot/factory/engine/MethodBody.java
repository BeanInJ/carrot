package com.carrot.factory.engine;

import java.lang.reflect.Method;

/**
 * 方法体
 *
 * MethodBody 是在扫描到类中的某个方法为“功能”方法后 new
 * {id,Method，Class}
 */
public class MethodBody {
    /**
     * 方法体唯一标识
     */
    private final String methodId;

    /**
     * 方法所在的类
     */
    private Class<?> clazz;
    /**
     * 方法
     */
    private final Method method;

    /**
     * 每一个 MethodBody 生成的时候，会自动往MethodEngine注册
     * 所以仅允许本包内方法创建 MethodBody
     */
    protected MethodBody(Method method,Class<?> clazz){
        this.method = method;
        this.clazz = clazz;
        this.methodId = clazz.getName() + "." + method.getName();
        MethodContainer.registerMethodBody(this.methodId,this);
    }

    public String getMethodId() {
        return methodId;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }
}
