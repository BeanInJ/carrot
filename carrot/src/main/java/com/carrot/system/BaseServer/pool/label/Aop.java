package com.carrot.system.BaseServer.pool.label;

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
public @interface Aop {
    /**
     * value是AOP要扫描的内容，格式：
     * “类全名.方法名”
     * “类全名.*”
     * “url:xx/xxx"
     * “url:xx/*"
     */
    String value();
}
