package com.vegetables.core;

import com.vegetables.annotation.BeforeEnter;
import com.vegetables.annotation.BeforeReturn;
import com.vegetables.annotation.Controller;
import com.vegetables.system.aop.AopPool;
import com.vegetables.system.aop.active.AOP;
import com.vegetables.system.dict.ConfigKey;
import com.vegetables.system.notch.YouCanChange;
import com.vegetables.util.PackageUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 扫描器
 */
public class InnerScanner implements YouCanChange {

    /**
     * 扫描得到的Controller
     */
    private static List<Class<?>> controllers;

    /**
     * 扫描得到的”请求之前“的拦截器
     */
    private static List<Class<?>> beforeEnters;

    /**
     * 扫描得到的”响应之前“的拦截器
     */
    private static List<Class<?>> beforeReturns;

    /**
     * 扫描器
     */
    public static void load(Class<?> main) {

        controllers = new ArrayList<>();
        beforeEnters = new ArrayList<>();
        beforeReturns = new ArrayList<>();

        try {
            // 扫描内部包
            scannerAnnotation("com.vegetables.system.baseServer");
            // 扫描外部包
            if (!main.getName().equals(App.class.getName())) {
                String outPackage = (String) InnerConfig.getConfig().get(ConfigKey.APP_START_PACKAGE);
                if (outPackage == null) {
                    outPackage = main.getPackage().toString().split(" ")[1];
                    InnerConfig.put(ConfigKey.APP_START_PACKAGE, outPackage);
                }
                scannerAnnotation(outPackage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static List<Class<?>> getControllers() { return controllers; }

    public static List<Class<?>> getBeforeEnters() {
        return beforeEnters;
    }

    public static List<Class<?>> getBeforeReturns() {
        return beforeReturns;
    }

    /**
     * 扫描注解
     */
    private static void scannerAnnotation(String packageName) throws ClassNotFoundException, IOException {
        List<String> classNames = PackageUtil.getClassName(packageName);
        for (String className : classNames) {
            Class<?> aClass = Class.forName(className);

            addToList(BeforeEnter.class, beforeEnters, aClass);
            addToList(Controller.class, controllers, aClass);
            addToList(BeforeReturn.class, beforeReturns, aClass);

            // AOP切面类
            if (aClass.isAnnotationPresent(AOP.class)) {
                AopPool.add(aClass);
            }
        }
    }

    /**
     * 添加到列表
     */
    private static void addToList(Class<? extends Annotation> annotationClazz,
                                  List<Class<?>> list,
                                  Class<?> clazz) {

        if (clazz.isAnnotationPresent(annotationClazz)) {
            if (!list.contains(clazz)) {
                list.add(clazz);
            }
        }
    }
}
