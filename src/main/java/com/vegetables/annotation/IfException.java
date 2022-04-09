package com.vegetables.annotation;

import java.lang.annotation.*;

/**
 * 拦截异常
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IfException {
}
