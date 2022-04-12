package com.vegetables.system.notch;

import com.vegetables.entity.BaseRequest;
import com.vegetables.entity.BaseResponse;

public interface BeforeEnterFunction {

    /**
     * 拦截后，更改请求体，并准备一个新的响应体（此响应体会被controller中的方法影响）
     */
    default BaseResponse beforeEnterAndReturn(BaseRequest request) {
        return null;
    }

    /**
     * 拦截后，仅更改请求体
     */
   default void beforeEnter(BaseRequest request){}

}
