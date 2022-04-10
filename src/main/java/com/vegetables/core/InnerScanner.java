package com.vegetables.core;

import com.vegetables.annotation.BeforeEnter;
import com.vegetables.annotation.BeforeReturn;
import com.vegetables.annotation.Controller;
import com.vegetables.system.notch.YouCanChange;
import com.vegetables.util.PackageUtil;

import java.io.IOException;
import java.util.*;

/**
 *
 */
public class InnerScanner implements YouCanChange {
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
            scannerAnnotation("com.vegetables.system.baseServer");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 外部扫描路径
        // 判断clazz是否是App
        if(!main.getName().equals(App.class.getName())){
            String outPackage = (String)InnerConfig.getConfig().get("app.scanner.package");
            if (outPackage == null){
                outPackage = main.getPackage().toString().split(" ")[1];
                InnerConfig.setMainPackage(outPackage);
            }

        System.out.println("outPackage:"+outPackage);
            try {
                scannerAnnotation(outPackage);
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    private static void scannerAnnotation(String packageName) throws ClassNotFoundException, IOException {
        List<String> classNames = PackageUtil.getClassName(packageName);
        for (String className : classNames) {
            // 加载类
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
