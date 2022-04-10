package com.vegetables.annotation;

import java.lang.annotation.*;

/**
 * 注册路由：
 * 一个路由方法的类名上必须有这个注解
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
    /** 入值为url */
    String value() default "/";
    boolean isCover() default false;
}
