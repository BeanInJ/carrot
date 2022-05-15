package com.easily.label;

import java.lang.annotation.*;

/**
 * 标记需要在系统启动之前执行的配置类
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Carrot
public @interface Configure {
}