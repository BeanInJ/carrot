package com.carrot.aop;

import com.carrot.system.BaseServer.pool.label.Aop;
import com.carrot.system.BaseServer.pool.AopPool;

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

    /**
     * 解析aopValue
     *
     * aopValue 可能出现的类型：
     * 1、XX.XX                  (匹配具体类，表示该类下的所有controller方法都接收切面)
     * 3、XX.XX*                 (模糊匹配类名)
     * 4、XX.XX.*                (匹配XX.XX包下所有类)
     * 5、XX.*.YY                (匹配XX包下所有子包中名叫YY的类)
     * 6、class:XX.XX            ( = XX.XX)
     * 7、method:XX.XX.xx        (匹配具体方法，仅该方法接收切面)
     * 8、package:包名            (匹配具体包，该包下的所有controller方法都接收切面)
     * 9、method:XX.*.YY.GG*     (匹配XX包下所有子包中名叫YY的类里的GG开头的方法)
     */
    public static List<String[]> parseAopValue(){
        return null;
    }

}
