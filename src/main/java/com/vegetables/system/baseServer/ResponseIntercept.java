package com.vegetables.system.baseServer;

import com.vegetables.annotation.BeforeReturn;
import com.vegetables.entity.BaseRequest;
import com.vegetables.entity.BaseResponse;
import com.vegetables.system.notch.BeforeReturnFunction;
import com.vegetables.util.StringUtils;

/**
 * Controller方法执行后追加内容
 */
@BeforeReturn
public class ResponseIntercept implements BeforeReturnFunction {
    @Override
    public void beforeReturn(BaseRequest request, BaseResponse response) {

        // 拦截404
        if(response.getStatus().equals("404")){
            response.setBody("404 - Not Found");
        }

        // 拦截空返回
        if(StringUtils.isNotBlankOrNull(response.getBody())){
            response.setBody(request.getUrl() + " - 返回 null");
        }
    }
}
