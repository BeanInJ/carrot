package com.carrot.factory.actuator;

import com.carrot.aop.*;
import com.carrot.factory.PoolActuator;
import com.carrot.system.BaseServer.pool.label.Aop;
import com.carrot.system.util.StringUtils;

import java.lang.annotation.Annotation;
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
     * 解析AopValue，向aop容器中推送
     */
    @Override
    public void parse(Class<?> clazz) {
        // 获取AOP注解中的value
        for (Annotation annotation : clazz.getAnnotations()) {
            if (annotation.annotationType().equals(Aop.class)) {
                Aop aop = (Aop) annotation;
                String value = aop.value();
                if(StringUtils.isBlankOrNull(value)) continue;
                value = AopHelper.parseAopValue(value);
                AopContainer.put(value,clazz);
            }
        }
    }

    @Override
    public <T> void execute(T t) {
        AopInvokeFlow flow = (AopInvokeFlow) t;
        new AopInvoke().invoke(flow);
    }

    public AopBody checkAndGet(String targetMethodName) {
        // 从缓存中找aop
        AopBody aopBody = AopTargetBodyContainer.get(targetMethodName);
        if(aopBody == null) {
            // 解析目标方法获取aop
            List<Class<?>> aop = AopContainer.getAop(targetMethodName);
            if(aop.isEmpty()) return null;

            aopBody = new AopBody(targetMethodName,aop);
            AopTargetBodyContainer.put(targetMethodName, aopBody);
        }
        return aopBody;
    }

}
