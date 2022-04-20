package com.carrot.factory.actuator;

import com.carrot.aop.AopHelper;
import com.carrot.factory.PoolActuator;
import com.carrot.system.BaseServer.pool.label.Aop;

import java.util.List;
import java.util.logging.Logger;

/**
 * aop执行器的功能主要是解析类、执行aop方法，其他的交给AopContainer执行
 */
public class AopActuator implements PoolActuator {
    private static final Logger log = Logger.getGlobal();

    @Override
    public void parse(Class<?> clazz) {
        // 获取AOP注解中的value
        Aop annotation = clazz.getAnnotation(Aop.class);
        String aopValue = annotation.value();

        // 获取aopValue中的类名
        String aopClassName;
        try {
            aopClassName = AopHelper.getAopClassName(aopValue);
        }catch (ArrayIndexOutOfBoundsException e){
            log.info("aop未扫描到："+aopValue);
            return;
        }

        // 将获取到的类名保存到aop容器


        // 如果aopValue中的类名 = 目标类名
//        if(targetObjectName.equals(aopClassName)){
//            String aopMethodName = AopHelper.getAopMethodName(aopValue);
//            // 如果aopValue中的方法名 = 目标方法名，则添加到list中
//            // 如果aopValue中的方法名 = “*” ，则代表所有方法都需要执行
//            if("*".equals(aopMethodName) ||targetMethodName.equals(aopMethodName)){
//                // 加入匹配列表
//                list.add(aClass);
//            }
//        }

        // 如果aopValue中的类名 = 目标类名
//        if(targetObjectName.equals(aopClassName)){
//            String aopMethodName = getAopMethodName(aopValue);
//            // 如果aopValue中的方法名 = 目标方法名，则添加到list中
//            // 如果aopValue中的方法名 = “*” ，则代表所有方法都需要执行
//            if("*".equals(aopMethodName) ||targetMethodName.equals(aopMethodName)){
//                // 加入匹配列表
//                list.add(aClass);
//            }
//        }
    }

}
