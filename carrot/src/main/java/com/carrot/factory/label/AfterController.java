package com.carrot.factory.label;

import java.lang.annotation.*;

/**
 * 标记后置过略器方法
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AfterController {
    int value() default 100;
}
