package com.vegetables.label.method;

import com.vegetables.entity.BaseRequest;
import com.vegetables.entity.BaseResponse;

public interface BeforeReturnFunction {
    /**
     * 拦截后更改响应体
     */
    void beforeReturn(BaseRequest request, BaseResponse response);
}
