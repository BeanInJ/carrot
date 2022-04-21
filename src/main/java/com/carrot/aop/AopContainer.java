package com.carrot.aop;

import com.carrot.aop.annotation.AopAfter;
import com.carrot.aop.annotation.AopBefore;
import com.carrot.aop.annotation.AopException;
import com.carrot.aop.annotation.AopFinally;
import com.carrot.factory.engine.MethodContainer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * aop方法容器
 */
public class AopContainer {
    // 目标方法全名
    private final Set<String> targetMethodNames = new HashSet<>();
    // 前置
    private final List<String> beforeMethods = new ArrayList<>();
    // 后置
    private final List<String> afterMethods = new ArrayList<>();
    // 异常
    private final List<String> catchMethods = new ArrayList<>();
    // 总是执行
    private final List<String> finallyMethods = new ArrayList<>();

    public List<String> getBeforeMethods() {
        return beforeMethods;
    }

    public List<String> getAfterMethods() {
        return afterMethods;
    }

    public List<String> getCatchMethods() {
        return catchMethods;
    }

    public List<String> getFinallyMethods() {
        return finallyMethods;
    }

    public boolean isTarget(String targetName){
        return targetMethodNames.contains(targetName);
    }

    protected void putTarget(String targetName){
        targetMethodNames.add(targetName);
    }

    protected void put(String methodId){
        Method method = MethodContainer.getMethod(methodId);
        if(method == null) return;

        if (method.isAnnotationPresent(AopBefore.class)) {
            this.beforeMethods.add(methodId);
        } else if (method.isAnnotationPresent(AopAfter.class)) {
            this.afterMethods.add(methodId);
        } else if (method.isAnnotationPresent(AopException.class)) {
            this.catchMethods.add(methodId);
        } else if (method.isAnnotationPresent(AopFinally.class)) {
            this.finallyMethods.add(methodId);
        }
    }

}
