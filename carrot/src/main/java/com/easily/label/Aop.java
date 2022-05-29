package com.easily.label;

import java.lang.annotation.*;

/**
 * AOP
 * 在前置拦截器后执行
 * 在后置拦截器前执行
 * 如果方法中产生异常，在异常拦截器前执行
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Carrot
public @interface Aop {

    /**
     * value匹配的是AOP要切入目标方法全名
     *
     * value的格式可以是
     * 正则表达式                 匹配目标方法全名 （包名+类名+方法名）
     * “method:" + 正则表达式     匹配目标方法名
     * ”class:“ + 正则表达式      匹配目标类名里的所有方法
     * ”package:“ + 正则表达式    匹配目标包名里的所有方法
     */
    String[] value();
}