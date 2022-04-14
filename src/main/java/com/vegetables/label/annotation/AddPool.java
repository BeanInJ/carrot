package com.vegetables.label.annotation;

import java.lang.annotation.*;

/**
 * 标记需要添加到工厂的类池
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AddPool {
}
