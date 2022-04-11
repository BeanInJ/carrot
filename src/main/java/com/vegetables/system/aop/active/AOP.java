package com.vegetables.system.aop.active;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AOP {
    /**
     * value是AOP要扫描的 “类全名+方法名”
     */
    String value();
}
