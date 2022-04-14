package com.vegetables.label.annotation;

import java.lang.annotation.*;

/**
 * 标记 控制器异常拦截类
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IfException {
}
