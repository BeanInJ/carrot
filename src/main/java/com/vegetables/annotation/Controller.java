package com.vegetables.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
    /** 入值为url */
    String value() default "/";
}
