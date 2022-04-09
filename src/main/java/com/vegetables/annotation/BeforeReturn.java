package com.vegetables.annotation;

import java.lang.annotation.*;

/**
 * 拦截器：在方法执行之后执行
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BeforeReturn {
    // 入值为拦截器的优先级
    int value() default 1;
}
