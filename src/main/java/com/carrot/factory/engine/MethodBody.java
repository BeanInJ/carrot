package com.carrot.factory.engine;

import java.lang.reflect.Method;

public class MethodBody {
    /**
     * 方法所在的类
     */
    private Object object;
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

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
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
}
