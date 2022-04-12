package com.vegetables.core;

import com.vegetables.core.factory.classPool.ClassFactory;
import com.vegetables.util.PackageUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.logging.Logger;

public class Scanner {
    private static final Logger log = Logger.getGlobal();

    public static void load(Class<?> mainClass) {

    }

    /**
     * 扫描注解,添加到class工厂
     */
    private void scannerAnnotation(String packageName){
        List<String> classNames;
        try {
            classNames = PackageUtil.getClassName(packageName);
        }catch (IOException e){
            e.printStackTrace();
            log.warning("异常包名：" + packageName);
            return;
        }

        for (String className : classNames) {
            try {
                Class<?> aClass = Class.forName(className);
                for (Annotation annotation : aClass.getAnnotations()) {
                    ClassFactory.addClass(aClass, annotation);
                }
            }catch (ClassNotFoundException e){
                log.info("未扫描到的类型："+className);
            }
        }

        // 清除工厂中不合格产品
        ClassFactory.trim();
    }
}
