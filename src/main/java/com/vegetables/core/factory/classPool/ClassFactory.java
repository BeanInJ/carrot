package com.vegetables.core.factory.classPool;

import com.vegetables.annotation.BeforeEnter;
import com.vegetables.annotation.BeforeReturn;
import com.vegetables.annotation.Controller;
import com.vegetables.core.factory.Pool;
import com.vegetables.system.aop.active.AOP;
import org.omg.CORBA.INVALID_TRANSACTION;
import sun.java2d.Disposer;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * ClassFactory 的实现参考了工厂模式
 *
 * 扫描器扫描到的所有 class 在这里统一处理
 */
public class ClassFactory {
    /**
     * 保存已注册的“池”, key：类池， value：对应的注解
     */
    private static final Map<Pool,Class<? extends Annotation>> classPoolMap = new HashMap<>();

    public static void load(){
        // 注册类池

        // 注册 Controller 控制器 类池
        addPool(new ControllerPool(), Controller.class);
        // 注册 BeforeEnter 前置拦截器 类池
        addPool(new BeforeEnterPool(), BeforeEnter.class);
        // 注册 BeforeReturn 后置拦截器 类池
        addPool(new BeforeReturnPool(), BeforeReturn.class);
        // 注册 AOP aop切面 类池
        addPool(new AopPool(), AOP.class);
    }

    /**
     * 注册一个"池",必须在扫描器扫描之前注册才有效
     */
    public static boolean addPool(Pool pool,Class<? extends Annotation> annotationType){
        // 如果put成功，则返回true
        return classPoolMap.put(pool,annotationType) == null;
    }

    /**
     * 根据注解类型获取对应的类池
     */
    public static void addClass(Class<?> clazz){
        classPoolMap.entrySet().stream()
                .filter(entry -> clazz.isAnnotationPresent(entry.getValue()))
                .forEach(entry -> entry.getKey().add(clazz));
    }

    /**
     * 获取所有注册的类池
     */
    public static List<Pool> getAllPool(){
        return new ArrayList<>(classPoolMap.keySet());
    }

    public static void trim(){
        for (Map.Entry<Pool, Class<? extends Annotation>> poolClassEntry : classPoolMap.entrySet()) {
            poolClassEntry.getKey().trim();
        }
    }

}
