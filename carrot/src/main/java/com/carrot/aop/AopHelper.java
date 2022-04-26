package com.carrot.aop;

import com.carrot.aop.annotation.*;
import com.carrot.system.BaseServer.pool.label.Aop;
import com.carrot.system.BaseServer.pool.AopPool;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
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
            Aop annotation = aClass.getAnnotation(Aop.class);
            String aopValue = annotation.value();

            // 获取aopValue中的类名
            String aopClassName;
            try {
                aopClassName = getAopClassName(aopValue);
            }catch (ArrayIndexOutOfBoundsException e){
                log.info("aop未扫描到："+aopValue);
                continue;
            }

            // 如果aopValue中的类名 = 目标类名
            if(targetObjectName.equals(aopClassName)){
                String aopMethodName = getAopMethodName(aopValue);
                // 如果aopValue中的方法名 = 目标方法名，则添加到list中
                // 如果aopValue中的方法名 = “*” ，则代表所有方法都需要执行
                if("*".equals(aopMethodName) ||targetMethodName.equals(aopMethodName)){
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
        String end = getAopMethodName(aopValue);
        if(aopValue.endsWith("*")){
            end = ".*";
        }else {
            end = "." + end;
        }
        return aopValue.replace(end,"");
    }

    public static void trimAopTargetClasses(List<Class<?>> classes){
        Iterator<Class<?>> clazzIterator = classes.iterator();
        out:
        while (clazzIterator.hasNext()) {
            Class<?> clazz = clazzIterator.next();

            // 循环方法，检查注解
            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(AopAfter.class)
                        || method.isAnnotationPresent(AopAround.class)
                        || method.isAnnotationPresent(AopBefore.class)
                        || method.isAnnotationPresent(AopException.class)
                        || method.isAnnotationPresent(AopFinally.class)) {
                    continue out;
                }
            }
            log.info("移除无效AOP：" + clazz.getName());
            classes.remove(clazz);
        }
    }


    /**
     * 解析aopValue，得到包列表
     */
    public static String parseAopValue(String aopValue){
        if(aopValue.startsWith("method:")){
            aopValue = aopValue.replace("method:","");
        }else if(aopValue.startsWith("class:")){
            aopValue = aopValue.replace("class:","") + "[\\S]+";
        }else if(aopValue.startsWith("package:")){
            aopValue = aopValue.replace("package:","") + "[\\S]+";
        }
        return aopValue;
    }
}
