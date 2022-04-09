package com.vegetables.core;

import com.vegetables.annotation.BeforeEnter;
import com.vegetables.annotation.BeforeReturn;
import com.vegetables.annotation.Controller;
import com.vegetables.system.notch.BeforeEnterFunction;
import com.vegetables.system.notch.BeforeReturnFunction;
import com.vegetables.system.notch.YouCanChange;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 *
 */
public class InnerScanner implements YouCanChange {
    private static final String PACKAGE_PREFIX = "src.main.java.";
    private static final String PACKAGE_INNER = "com.vegetables.system.baseServer";
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

    public static void load() {
        controllers = new LinkedList<>();
        beforeEnters = new LinkedList<>();
        beforeReturns = new LinkedList<>();

        try {
            String packageName = PACKAGE_PREFIX + PACKAGE_INNER;
            String packageString = (String) InnerConfig.getConfig().get("scanner.package");
            if(packageString != null){
                packageName = PACKAGE_PREFIX + packageString;
            }
            scanner(packageName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据包名扫描
     */
    private static void scanner(String packageName) throws ClassNotFoundException {

        String fileName = packageName.replace('.', '\\');
        File file = new File(fileName);
        scannerByFile(file);
    }

    /**
     * 根据文件扫描
     */
    private static void scannerByFile(File file) throws ClassNotFoundException {

        boolean isJava = file.isFile() && file.getName().endsWith(".java");
        if (isJava) {
            String filePath = file.getPath();

            // 将路径换成包名
            String qualifiedName = filePath.substring(PACKAGE_PREFIX.length(), filePath.length() - 5)
                    .replace('\\', '.');

            Class<?> aClass = Class.forName(qualifiedName);

            if (aClass.isAnnotationPresent(BeforeEnter.class)) {
                beforeEnters.add(aClass);
            }else if(aClass.isAnnotationPresent(Controller.class)){
                controllers.add(aClass);
            }else if(aClass.isAnnotationPresent(BeforeReturn.class)){
                beforeReturns.add(aClass);
            }

        } else if (file.isDirectory()) {
            for (File f : Objects.requireNonNull(file.listFiles()))
                scannerByFile(f);
        }
    }

    public static List<Class<?>> getControllers() {
        return controllers;
    }

    public static List<Class<?>> getBeforeEnters() {
        return beforeEnters;
    }

    public static List<Class<?>> getBeforeReturns() {
        return beforeReturns;
    }
}
