package com.vegetables.system.aop.entity;

import java.util.List;

/**
 * 一个目标方法对应多个aop方法
 */
public class AopLinkTarget {
    /**
     * aop切入时要执行的方法
     */
    private List<AopMethodAndClass> aopMethods;
    /**
     * 目标对象最终要执行的方法
     */
    private AopMethodAndClass targetMethod;

    public List<AopMethodAndClass> getAopMethods() {
        return aopMethods;
    }

    public void setAopMethods(List<AopMethodAndClass> aopMethods) {
        this.aopMethods = aopMethods;
    }

    public AopMethodAndClass getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(AopMethodAndClass targetMethod) {
        this.targetMethod = targetMethod;
    }
}
