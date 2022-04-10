package com.vegetables.core;

import com.vegetables.annotation.BeforeEnter;
import com.vegetables.annotation.BeforeReturn;
import com.vegetables.annotation.Controller;
import com.vegetables.system.dict.ConfigKey;
import com.vegetables.system.notch.YouCanChange;
import com.vegetables.util.PackageUtil;

import java.io.IOException;
import java.util.*;

/**
 *
 */
public class InnerScanner implements YouCanChange {

    // 此处待优化，
    // 1、扫描到的类列表，不应该用Set，这些列表的 取出次数 大于添加
    // 2、考虑是否在扫描的时候，就把所有的类都加载进来，这样就不需要每次用的时候加载类（考虑类的加载次数）
    // 2、解耦

    /**
     * 扫描得到的Controller
     */
    private static Set<Class<?>> controllers;

    /**
     * 扫描得到的”请求之前“的拦截器
     */
    private static Set<Class<?>> beforeEnters;

    /**
     * 扫描得到的”响应之前“的拦截器
     */
    private static Set<Class<?>> beforeReturns;

    public static void load(Class<?> main) {

        controllers = new HashSet<>();
        beforeEnters = new HashSet<>();
        beforeReturns = new HashSet<>();

        try {
            // 扫描内部包
            scannerAnnotation("com.vegetables.system.baseServer");
            // 扫描外部包
            if(!main.getName().equals(App.class.getName())){
                String outPackage = (String)InnerConfig.getConfig().get(ConfigKey.APP_START_PACKAGE);
                if (outPackage == null){
                    outPackage = main.getPackage().toString().split(" ")[1];
                    InnerConfig.put(ConfigKey.APP_START_PACKAGE,outPackage);
                }
                scannerAnnotation(outPackage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Set<Class<?>> getControllers() {
        return controllers;
    }

    public static Set<Class<?>> getBeforeEnters() {
        return beforeEnters;
    }

    public static Set<Class<?>> getBeforeReturns() {
        return beforeReturns;
    }

    /**
     * 扫描注解
     */
    private static void scannerAnnotation(String packageName) throws ClassNotFoundException, IOException {
        List<String> classNames = PackageUtil.getClassName(packageName);
        for (String className : classNames) {
            Class<?> aClass = Class.forName(className);
            if (aClass.isAnnotationPresent(BeforeEnter.class)) {
                beforeEnters.add(aClass);
            }else if(aClass.isAnnotationPresent(Controller.class)){
                controllers.add(aClass);
            }else if(aClass.isAnnotationPresent(BeforeReturn.class)){
                beforeReturns.add(aClass);
            }
        }
    }

}
