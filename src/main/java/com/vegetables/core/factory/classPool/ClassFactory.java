package com.vegetables.core.factory.classPool;

import com.vegetables.annotation.BeforeEnter;
import com.vegetables.annotation.BeforeReturn;
import com.vegetables.annotation.Controller;
import com.vegetables.system.aop.active.AOP;

import java.lang.annotation.Annotation;

/**
 * ClassFactory 的实现参考了工厂模式
 */
public class ClassFactory {
    public static void addClass(Class<?> clazz, Annotation annotation){
        Class<? extends Annotation> annotationType = annotation.annotationType();

        if(Controller.class.equals(annotationType)){
                // 添加到控制器
                ControllerPool.add(clazz);
            }else if(BeforeEnter.class.equals(annotationType)){
                // 添加到前置拦截器
                BeforeEnterPool.add(clazz);
            }else if(BeforeReturn.class.equals(annotationType)){
                // 添加到后置拦截器
                BeforeReturnPool.add(clazz);
            }else if(AOP.class.equals(annotationType)){
                // 添加到AOP
                AopPool.add(clazz);
            }
    }

    public static void trim(){
        ControllerPool.trim();
        BeforeEnterPool.trim();
        BeforeReturnPool.trim();
        AopPool.trim();
    }

}
