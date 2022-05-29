package com.easily.label;

import javax.annotation.Resource;
import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Carrot
@Resource
public @interface Service {
    String value() default "";
}
