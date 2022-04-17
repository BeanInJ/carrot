package com.carrot.system.BaseServer.filter;

import com.carrot.core.http.BaseRequest;
import com.carrot.core.http.BaseResponse;
import com.carrot.system.BaseServer.pool.FilterBody;
import com.carrot.system.BaseServer.pool.label.AfterController;
import com.carrot.system.BaseServer.pool.label.BeforeController;
import com.carrot.system.BaseServer.pool.label.Filter;
import com.carrot.system.util.StringUtils;

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

    /**
     * 拦截404、空返回
     */
    @AfterController
    public void after(FilterBody filterBody){
        BaseRequest request = filterBody.getRequest();
        BaseResponse response = filterBody.getResponse();

        // 拦截404
        if(response.getStatus().equals("404")){
            response.setBody("404 - Not Found : "+ request.getUrl());
            response.setReason("Not Found");
        }
        System.out.println(request.getUrl() + " : " + response.getStatus());


        // 拦截空返回
        if(StringUtils.isBlankOrNull(response.getBody())){
            response.setBody(request.getUrl() + " - 返回 null");
        }
    }
}

