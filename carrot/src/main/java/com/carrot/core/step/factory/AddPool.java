package com.carrot.core.step.factory;

import java.lang.annotation.*;

/**
 * 标记需要添加到 “类工厂” 的 ”类池“
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AddPool {
}