package com.vegetables.label.method;

import com.vegetables.entity.BaseRequest;
import com.vegetables.entity.BaseResponse;

/**
 * 后置拦截器必须实现这个类
 */
public interface BeforeReturnFunction {
    /**
     * 拦截后更改响应体
     */
    void beforeReturn(BaseRequest request, BaseResponse response);
}
