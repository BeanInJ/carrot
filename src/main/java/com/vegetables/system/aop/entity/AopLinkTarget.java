package com.vegetables.system.aop.entity;

import java.util.List;

/**
 * 一个目标方法对应多个aop方法
 */
public class AopLinkTarget {
    /**
     * aop切入时要执行的方法
     */
    private List<MethodAndClass> aopMethods;
    /**
     * 目标对象最终要执行的方法
     */
    private MethodAndClass targetMethod;

    public List<MethodAndClass> getAopMethods() {
        return aopMethods;
    }

    public void setAopMethods(List<MethodAndClass> aopMethods) {
        this.aopMethods = aopMethods;
    }

    public MethodAndClass getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(MethodAndClass targetMethod) {
        this.targetMethod = targetMethod;
    }
}
