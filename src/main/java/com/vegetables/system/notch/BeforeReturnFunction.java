package com.vegetables.system.notch;

import com.vegetables.entity.HttpGetter;
import com.vegetables.entity.HttpSetter;

public interface BeforeReturnFunction {
    /**
     * 拦截后更改响应体
     */
    void beforeReturn(HttpGetter request,HttpSetter httpSetter);
}
