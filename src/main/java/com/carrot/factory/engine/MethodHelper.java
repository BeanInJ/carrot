package com.carrot.factory.engine;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 辅助MethodBody的生成
 */
public class MethodHelper {
    /**
     * 从类中获取指定判断条件下的方法体列表
     * 并向方法容器注册
     */
    public static List<String> initMethodBodyList(Class<?> clazz, Condition condition){
        List<String> list = new ArrayList<>();
        for (Method method : clazz.getMethods()) {
            if(condition.execute()){
                list.add(initMethodBody(clazz,method));
            }
        }
        return list;
    }

    /**
     * 从类中获取含有指定注解的方法体列表
     * 并向方法容器注册
     */
    public static List<String> initMethodBodyList(Class<?> clazz, Class<? extends Annotation> annotationClazz){
        List<String> list = new ArrayList<>();
        for (Method method : clazz.getMethods()) {
            if(method.isAnnotationPresent(annotationClazz)){
                list.add(initMethodBody(clazz,method));
            }
        }
        return list;
    }

    /**
     * 从类中获取含有指定注解、指定判断条件的方法体列表
     * 并向方法容器注册
     */
    public static List<String> initMethodBodyList(Class<?> clazz, Class<? extends Annotation> annotationClazz,Condition condition){
        List<String> list = new ArrayList<>();
        for (Method method : clazz.getMethods()) {
            if(method.isAnnotationPresent(annotationClazz) && condition.execute()){
                list.add(initMethodBody(clazz,method));
            }
        }
        return list;
    }

    /**
     * 每一个 MethodBody 生成后，会自动往 MethodEngine 中注册
     * 生成 MethodBody，返回其ID
     */
    public static String initMethodBody(Class<?> clazz,Method method){
        MethodBody methodBody = new MethodBody(method, clazz);
        return methodBody.getMethodId();
    }

}
