package com.vegetables.label.annotation;

import java.lang.annotation.*;

/**
 * 标记拦截器：在目标方法执行之前执行
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
