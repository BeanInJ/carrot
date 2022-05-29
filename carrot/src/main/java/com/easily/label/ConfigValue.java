package com.easily.label;

import javax.annotation.Resource;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Resource
public @interface ConfigValue {
    String value();
}
