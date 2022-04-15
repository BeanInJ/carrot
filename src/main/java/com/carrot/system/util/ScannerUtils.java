package com.carrot.system.util;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 扫描器
 */
public class ScannerUtils {
    private static final Logger log = Logger.getGlobal();

    /**
     * 扫描含某个注解的
     */
    public static List<Class<?>> getClassesAtAnnotation(String packageName,
                                                         Class<? extends Annotation> annotationClazz){

        List<Class<?>> list = new ArrayList<>();
        for (String className : getClassNames(packageName)) {
            try {
                Class<?> aClass = Class.forName(className);
                if(aClass.isAnnotationPresent(annotationClazz)){
                    list.add(aClass);
                }
            }catch (ClassNotFoundException ignored){}
        }
        return list;
    }

    /**
     * 扫描有注解的
     */
    public static List<Class<?>> getClassesAtAnnotation(String packageName){
        List<Class<?>> list = new ArrayList<>();
        for (String className : getClassNames(packageName)) {
            try {
                Class<?> aClass = Class.forName(className);
                if(aClass.getAnnotations().length != 0) {
                    list.add(aClass);
                }
            }catch (ClassNotFoundException ignored){}
        }
        return list;
    }

    /**
     * 扫描所有
     */
    public static List<Class<?>> getClassesAll(String packageName){
        List<Class<?>> list = new ArrayList<>();
        for (String className : getClassNames(packageName)) {
            try {
                Class<?> aClass = Class.forName(className);
                list.add(aClass);
            }catch (ClassNotFoundException ignored){}
        }
        return list;
    }

    private static List<String> getClassNames(String packageName){
        List<String> list = new ArrayList<>();
        try {
            list = PackageUtil.getClassName(packageName);
            return list;
        }catch (IOException e){
            e.printStackTrace();
            log.warning("异常包名：" + packageName);
            return list;
        }
    }
}
