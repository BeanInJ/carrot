package com.easily.aop;

import com.easily.factory.Pool;
import com.easily.factory.aop.AopContainer;
import com.easily.factory.aop.AopMethod;
import com.easily.factory.aop.AopPool;

import java.lang.reflect.Method;

// 测试aop
public class AopPoolTest {
    public static void main(String[] args) throws InterruptedException {
        testAop();
    }

    // 测试aop方法多层包裹
    public static void testAop() throws InterruptedException {
        // 创建池，这个过程在Carrot中由class工厂创建，不需要手动创建
        Pool pool= new AopPool();

        pool.add(AddAopTarget.class);
        pool.add(AddAopTarget1.class);
        pool.add(AddAopTarget2.class);

        pool.parseToContainer();

        // 获取到Aop产品容器
        AopContainer container = pool.getProductContainer(AopContainer.class);

        Class<?> clazz = Target.class;
        for (Method method : clazz.getDeclaredMethods()) {
            try {
                // 从容器或缓存中获取
                AopMethod aopMethod = container.getAopMethod(method, clazz);
                if (aopMethod != null){
                    Object[] args = {"ss",1};
                    aopMethod.invoke(args);
                }
//                soutObject(aopMethod);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 循环打印AopMethod的aopObjectName
     */
    public static void soutObject(AopMethod method){
        Method method1 = method.getMethod();
        if(method1 == null){
            soutObject(method.getRootAopMethod());
        }
        System.out.println(method.getAopObject().getClass().getName());
    }
}
