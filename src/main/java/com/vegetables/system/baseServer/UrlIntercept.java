package com.vegetables.system.baseServer;

import com.vegetables.label.annotation.BeforeEnter;
import com.vegetables.entity.BaseRequest;
import com.vegetables.entity.BaseResponse;
import com.vegetables.label.method.BeforeEnterFunction;

/**
 * 拦截 /?
 */
@BeforeEnter(0)
public class UrlIntercept implements BeforeEnterFunction {
    public BaseResponse beforeEnterAndReturn(BaseRequest request){
        String url = request.getUrl();
        if (url.equals("/?")){
            BaseResponse response = new BaseResponse();
            response.setStatus("300");
            response.setReason("?");
            response.setBody("想做点什么呢？");

            // 直接返回前端，不再执行控制器或后续拦截器
            response.setReturnNow(true);
            return response;
        }

        return null;
    }
}
