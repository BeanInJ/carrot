package com.carrot.aop;

import com.carrot.aop.annotation.AopAfter;
import com.carrot.aop.annotation.AopBefore;
import com.carrot.aop.annotation.AopException;
import com.carrot.aop.annotation.AopFinally;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * aop方法容器
 */
public class AopBody {
    // 目标方法全名
    private final String targetMethodName;
    private final Map<Method,Object> beforeMethods = new HashMap<>();
    private final Map<Method,Object> afterMethods = new HashMap<>();
    private final Map<Method,Object> catchMethods = new HashMap<>();
    private final Map<Method,Object> finallyMethods = new HashMap<>();
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

    public AopBody(String targetMethodName) {
        this.targetMethodName = targetMethodName;
    }

    public AopBody(String targetMethodName, List<Class<?>> clazzs) {
        this.targetMethodName = targetMethodName;
        for (Class<?> clazz : clazzs) {
            put(clazz);
        }
    }

    public String getTargetMethodName() {
        return targetMethodName;
    }

    public Map<Method, Object> getBeforeMethods() {
        return beforeMethods;
    }

    public Map<Method, Object> getAfterMethods() {
        return afterMethods;
    }

    public Map<Method, Object> getCatchMethods() {
        return catchMethods;
    }

    public Map<Method, Object> getFinallyMethods() {
        return finallyMethods;
    }

    protected void put(Class<?> clazz){
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(AopBefore.class)) {
                this.beforeMethods.put(method,clazz);
            } else if (method.isAnnotationPresent(AopAfter.class)) {
                this.afterMethods.put(method,clazz);
            } else if (method.isAnnotationPresent(AopException.class)) {
                this.catchMethods.put(method,clazz);
            } else if (method.isAnnotationPresent(AopFinally.class)) {
                this.finallyMethods.put(method,clazz);
            }
        }
    }
}
