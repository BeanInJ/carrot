package com.carrot.factory.actuator;

import com.carrot.aop.AopHelper;
import com.carrot.factory.PoolActuator;
import com.carrot.system.BaseServer.pool.label.Aop;

import java.util.List;
import java.util.logging.Logger;

/**
 * aop执行逻辑：
 * 1、调用aop检查：AopActuator.isAopTarget(类全名.方法名)
 * 2、检查该方法名是否在aop目标方法容器中
 * 3、如果在，将该方法包裹aop切面方法后执行
 *
 * 注意，如果多个aop方法拦截到同一个目标方法，aop方法是无序的
 */
public class AopActuator implements PoolActuator {
    private static final Logger log = Logger.getGlobal();

    /**
     * 解析得到aop方法体，向aop容器中推送
     */
    @Override
    public void parse(Class<?> clazz) {
        // 获取AOP注解中的value
        Aop annotation = clazz.getAnnotation(Aop.class);
        String aopValue = annotation.value();

        // 获取aopValue中的类全名列表
        List<String> packages;
        try {
            packages = AopHelper.parseAopValue(aopValue);
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
