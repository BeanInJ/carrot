package com.carrot.system.BaseServer.filter;

import com.carrot.core.http.BaseRequest;
import com.carrot.core.http.BaseResponse;
import com.carrot.system.BaseServer.pool.FilterBody;
import com.carrot.system.BaseServer.pool.label.BeforeController;
import com.carrot.system.BaseServer.pool.label.Filter;

@Filter
public class BaseFilter {

    /**
     * 拦截url /？
     */
    @BeforeController
    public BaseResponse before(FilterBody filterBody){
        // 设置优先级,数字越小优先级越高
        filterBody.setPriority(50);
        BaseRequest request = filterBody.getRequest();
        BaseResponse response = filterBody.getResponse();

        String url = request.getUrl();
        if (url.equals("/?")){
            if(response == null) {
                response = new BaseResponse();
                response.setStatus("300");
                response.setReason("?");
                response.setBody("想做点什么呢？");

                // 直接返回前端，不再执行控制器或后续拦截器
                response.setReturnNow(true);
                return response;
            }
        }
        return response;
    }
}

