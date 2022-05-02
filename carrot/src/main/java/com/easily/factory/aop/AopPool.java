package com.easily.factory.aop;

import com.easily.factory.ClassPool;
import com.easily.label.*;
import com.easily.system.dict.INNER;
import com.easily.system.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * aop池：生产Map{匹配目标的正则,AopClass}
 *
 * 1、aop池解析一个类的方法时，只解析本类的方法，不解析父类的方法
 * 2、aop目标是不区分方法的重载的，也就是一个类里面有两个方法，方法名相同、参数类型不同，这样的两个方法会被aop识别为同一目标
 *
 * 名词解释：
 * aop目标方法：需要被切入功能的方法
 * aop功能方法：切入目标方法的功能，类型有：前置切入、后置切入、异常切入、最终切入
 * aop方法（AopMethod）：目标方法包裹着aop功能后，最终生成的实体。
 *
 * 注意：
 * 经过多层包裹的aop方法”最终切面“，会在当前层”后置“方法之后接着执行，也就是在下一层”后置“方法之前执行
 *
 *
 * aop包下几个类的作用：
 * AopPool ：接收工厂分发下来的原料，解析成“产品”放入AopContainer中，等待被使用
 * AopContainer ： 存放“产品”,提供Aop目标检查与AopMethod封装
 * AopMethodCache： 临时缓存已经封装好的AopMethod，避免每次目标方法被调用都需要重新封装
 * AopMethod：经过aop封装的方法体，其invoke方法可直接调用
 * MethodBody：存储目标方法需要的类、参数、返回、异常等信息，每次AopMethod被调用时，会new一个
 * AopMethodActuator: AopMethod 内置执行器仅aop包内可调用，辅助AopMethod的切面调用、MethodBody的创建
 *
 */
public class AopPool extends ClassPool {
    private static final Logger log = Logger.getGlobal();

    // 产品容器
    private final AopContainer container = new AopContainer();

    @Override
    public Class<? extends Annotation> getLabel() {
        return Aop.class;
    }

    @Override
    public String getPoolName() {
        return INNER.AOP_POOl_NAME;
    }

    /**
     * 以AopValue为key，clazz为value，存入aop容器
     */
    @Override
    public void parseToContainer() {

        out:
        for (Class<?> clazz : this.classes) {
            // clazz的方法不包含aop方法的，不加入容器
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(AopAfter.class)
                        || method.isAnnotationPresent(AopAround.class)
                        || method.isAnnotationPresent(AopBefore.class)
                        || method.isAnnotationPresent(AopException.class)
                        || method.isAnnotationPresent(AopFinally.class)) {

                    // 如果有aop方法，则加入容器
                    break;
                }else {
                    // 没有aop方法,检查下一个类
                    continue out;
                }
            }

            // class和正则的关系：
            // 一个class可以有多个正则
            // 一个class只能在容器中出现一次
            // 整个容器中，正则是可以重复的，但是不能在一个类上重复

            List<String> aopValues = new ArrayList<>();
            for (Annotation annotation : clazz.getAnnotations()) {
                if (annotation.annotationType().equals(Aop.class)) {
                    Aop aop = (Aop) annotation;
                    String value = aop.value();
                    if (StringUtils.isBlankOrNull(value)) continue;
                    value = parseAopValue(value);
                    aopValues.add(value);
                }
            }
            container.put(clazz,aopValues);
        }
    }

    /**
     * 获取产品容器
     */
    @Override
    public <T> T getProductContainer(Class<T> clazz) {
        return clazz.isAssignableFrom(AopContainer.class)? clazz.cast(this.container):null;
    }

    /**
     * 解析aopValue，得到包列表
     */
    private String parseAopValue(String aopValue) {
        if (aopValue.startsWith("method:")) {
            aopValue = aopValue.replace("method:", "[\\S]*");
        } else if (aopValue.startsWith("class:")) {
            aopValue = aopValue.replace("class:", "") + "[\\S]+";
        } else if (aopValue.startsWith("package:")) {
            aopValue = aopValue.replace("package:", "") + "[\\S]+";
        }else {
            aopValue = aopValue + "[\\S]*";
        }
        return aopValue;
    }
}
