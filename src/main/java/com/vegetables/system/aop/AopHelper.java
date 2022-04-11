package com.vegetables.system.aop;

import com.vegetables.system.aop.active.AOP;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AopHelper {
    private static final Logger log = Logger.getGlobal();

    /**
     * 从扫描得到的aop池中，获取匹配的类
     */
    public static List<Class<?>> getMethodAop(Method targetMethod, Object targetObject){
        List<Class<?>> list = new ArrayList<>();
        String targetObjectName = targetObject.getClass().getName();
        String targetMethodName = targetMethod.getName();

        // 循环Aop池中的类
        for (Class<?> aClass : AopPool.getClasses()) {
            // 获取AOP注解中的value
            AOP annotation = aClass.getAnnotation(AOP.class);
            String aopValue = annotation.value();

            // 获取aopValue中的类名
            String aopClassName = null;
            try {
                aopClassName = getAopClassName(aopValue);
            }catch (ArrayIndexOutOfBoundsException e){
                log.info("未扫描到："+aopValue);
                continue;
            }

            // 如果aopValue中的类名 = 目标类名
            if(targetObjectName.equals(aopClassName)){
                String aopMethodName = getAopMethodName(aopValue);
                // 如果aopValue中的方法名 = 目标方法名
                if(targetMethodName.equals(aopMethodName)){
                    // 加入匹配列表
                    list.add(aClass);
                }
            }
        }
        return list;
    }

    /**
     * aopValue = ”类全名.方法名“
     * 获取 方法名
     */
    public static String getAopMethodName(String aopValue){
        String[] array = aopValue.split("\\.");
        return array[array.length-1];
    }

    /**
     * aopValue = ”类全名.方法名“
     * 获取 类全名
     */
    public static String getAopClassName(String aopValue){
        return aopValue.replace("."+getAopMethodName(aopValue),"");
    }


}
