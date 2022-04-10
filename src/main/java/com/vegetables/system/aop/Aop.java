package com.vegetables.system.aop;

import com.vegetables.system.aop.entity.AopLinkTarget;
import com.vegetables.system.aop.entity.AopMethodAndClass;
import com.vegetables.system.aop.entity.AopMethods;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Aop {
    public static void before(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        // 匹配当前method，然后执行
        for (AopLinkTarget before : new AopMethods().getBefores()) {
            if(before.getTargetMethod().getMethod() == method){
                for (AopMethodAndClass aopMethod : before.getAopMethods()) {
                    aopMethod.getMethod().invoke(aopMethod.getObject(),args);
                }
            }
        }

        System.out.println("before");
    }

    public static void after(Object proxy, Method method, Object[] args,Object returnValue) {
        System.out.println("after");
    }

    public static void catchException(Method method,Exception e,Object returnValue) {
        System.out.println("afterReturning");
    }

    public static void finallyTty(Object proxy, Method method, Object[] args,Object returnValue) {
        System.out.println("afterThrowing");
    }

    public static void around(Object proxy, Method method, Object[] args, Object returnValue) {
        System.out.println("around");
    }
}
