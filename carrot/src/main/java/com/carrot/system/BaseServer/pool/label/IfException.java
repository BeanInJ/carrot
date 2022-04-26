package com.carrot.system.BaseServer.pool.label;

import java.lang.annotation.*;

/**
 * 标记异常拦截器
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IfException {
}
