package com.vegetables.system.aop.entity;

import java.lang.reflect.Method;

public class MethodBody {
    /**
     * 方法所在的类
     */
    private Object proxy;
    /**
     * 方法
     */
    private Method method;
    /**
     * 参数列表
     */
    private Object[] args;
    /**
     * 返回值(优先初始化)
     */
    private Object returnValue;
    /**
     * 方法执行过程中产生的异常
     */
    private Exception e;

    /**
     * 是否立即返回
     */
    private boolean isReturnNow = false;

    /**
     * 是否执行下一步
     */
    private boolean isContinue = true;

    /**
     * 记录方法执行到哪一步了 (计数器)
     * 理想最终值一定是5，因为Finally一定会被执行，
     * 非理想情况：Finally在计速器计数器前产生异常
     */
    private int step = 0;

    public MethodBody(Object proxy, Method method, Object[] args) {
        this.proxy = proxy;
        this.method = method;
        this.args = args;
        this.returnValue = null;
        this.e = null;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void next() {
        this.step ++;
    }

    public boolean isReturnNow() {
        return isReturnNow;
    }

    public void setReturnNow(boolean returnNow) {
        isReturnNow = returnNow;
    }

    public boolean isContinue() {
        return isContinue;
    }

    public void setContinue(boolean aContinue) {
        isContinue = aContinue;
    }

    public Object getProxy() {
        return proxy;
    }

    public void setProxy(Object proxy) {
        this.proxy = proxy;
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

    public Exception getException() {
        return e;
    }

    public void setException(Exception e) {
        this.e = e;
    }
}
