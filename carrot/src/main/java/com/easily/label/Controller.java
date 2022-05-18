package com.easily.label;

import javax.annotation.Resource;
import java.lang.annotation.*;

/**
 * 标记这是一个控制器
 * 路由类、方法名上必须都有这个注解
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Carrot
@Resource
public @interface Controller {
    /** url */
    String value() default "/";

    /**
     * ture 强制覆盖已存在的url
     */
    boolean isCover() default false;
}