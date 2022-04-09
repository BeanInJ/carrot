package com.vegetables.core;

import com.vegetables.annotation.Controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 静态资源类：扫描含有Controller注解的类
 */
public class ControllerScanner {
    private static final String PACKAGE_PREFIX = "src.main.java.";
    public static final String PACKAGE_NAME = "com.vegetables.controller";
    private static List<Class<?>> classes;

    private ControllerScanner(){}

    // 配置文件扫描
    private static void fileScanner(File file) throws ClassNotFoundException {
        boolean isJava = file.isFile() && file.getName().endsWith(".java");
        if (isJava) {
            String filePath = file.getPath();
            // 将路径换成包名
            String qualifiedName = filePath.substring(PACKAGE_PREFIX.length(), filePath.length() - 5).replace('\\', '.');
            Class<?> aClass = Class.forName(qualifiedName);
            boolean haveAnnotation = aClass.isAnnotationPresent(Controller.class);
            if (haveAnnotation) {
                classes.add(aClass);
            }
        } else if (file.isDirectory()) {
            for (File f : Objects.requireNonNull(file.listFiles()))
                fileScanner(f);
        }
    }

    /*
     * 得到加载到的类对象的List,返回的是ArrayList
     */
    public static List<Class<?>> getClasses() {
        return classes;
    }

    protected static void load(){
        String filePath = (PACKAGE_PREFIX+PACKAGE_NAME).replace('.', '\\');
        classes = new ArrayList<>();
        try {
            fileScanner(new File(filePath));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
