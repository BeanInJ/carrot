package com.carrot.factory.engine;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 方法体
 * {Method，Class，参数[]，返回值}
 */
public class MethodBody {
    /**
     * 方法体唯一标识
     */
    private String methodId;

    /**
     * 方法所在的类
     */
    private Class<?> clazz;
    /**
     * 方法
     */
    private Method method;
    /**
     * 参数列表
     */
    private Object[] args;
    /**
     * 返回值
     */
    private Object returnValue;

    /**
     * 每一个 MethodBody 生成的时候，会自动往MethodEngine注册
     * 所以仅允许本包内方法创建 MethodBody
     */
    protected MethodBody(){
        this.methodId = UUID.randomUUID().toString();
        // 向MethodEngine注册
        MethodContainer.put(this.methodId,this);
    }

    /**
     * 获取方法体唯一标识
     */
    public String getMethodId() {
        return methodId;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }
}
