package com.vegetables.label.annotation;

import java.lang.annotation.*;

/**
 * 标记后置拦截器
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BeforeReturn {
    // 入值为拦截器的优先级
    int value() default 1;
}
