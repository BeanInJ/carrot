package com.vegetables.label.annotation;

import java.lang.annotation.*;

/**
 * 标记前拦截器
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BeforeEnter {
    /**
     * 拦截器优先级
     */
    int value() default 1;
}
