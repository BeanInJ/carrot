package com.carrot.factory.label;

import java.lang.annotation.*;

/**
 * 标记前置过滤器方法
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BeforeController {
    int value() default 100;
}
