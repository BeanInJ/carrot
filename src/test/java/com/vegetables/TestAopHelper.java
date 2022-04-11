package com.vegetables;


import com.vegetables.core.RequestThread;
import com.vegetables.system.aop.AopHelper;
import com.vegetables.system.aop.Nanny;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.vegetables.system.aop.AopHelper.getMethodAop;

/**
 * 测试aop
 */
public class TestAopHelper {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
//        List<Class<?>> list = new ArrayList<>();
//        list.add(BeforeMethodRun.class);
//        AopPool.setClasses(list);
//        if(AopPool.getClasses().isEmpty()) return;
//
//        AopHelper aopHelper = new AopHelper();
//        Class<?> aClass = aopHelper.getClass();
//        Method[] methods = aClass.getMethods();
//        for(Method method:methods){
//            for (Class<?> aClass1 : getMethodAop(method, aopHelper)) {
//                System.out.println("有一个匹配到了");
//            }
//        }
        AopHelper aopHelper = new AopHelper();
        Class<?> aClass = aopHelper.getClass();
        Object o = aClass.newInstance();
        Object o1 = o.getClass().newInstance();
        System.out.println(o1.hashCode());
    }
}
