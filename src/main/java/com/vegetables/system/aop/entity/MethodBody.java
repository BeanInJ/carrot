package com.vegetables.system.aop.entity;

import java.lang.reflect.Method;
import java.util.List;

/**
 * MethodBody中包含了aop目标方法的信息，包括所在类、方法名，方法参数，方法返回值、异常内容等
 */
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

    /**
     * 记录都执行了哪些方法
     */
    private List<Integer> stepList;

    public MethodBody(Object object, Method method, Object[] args) {
        this.object = object;
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

    public Exception getException() {
        return e;
    }

    public void setException(Exception e) {
        this.e = e;
    }
}
