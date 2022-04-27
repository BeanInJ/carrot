package com.easily.system.baseServer;

import com.easily.core.http.BaseRequest;
import com.easily.core.http.BaseResponse;
import com.easily.core.http.GetRequest;
import com.easily.factory.filter.FilterBody;
import com.easily.label.AfterController;
import com.easily.label.BeforeController;
import com.easily.label.Filter;
import com.easily.system.util.StringUtils;

/**
 * 过滤器创建规则：
 * 1、类名上必须有 @Filter注解
 * 2、方法上有@BeforeController（前置过滤）、@AfterController（后置过滤）
 * 3、每个过滤都有一个FilterBody参数可以接收，FilterBody可以获取request、response、过滤器优先级等信息
 * 4、关于过滤器的优先级，可以在FilterBody中设置，数值越小，优先级越高，默认值为1000
 * 5、关于返回值，可以是BaseResponse或者空，其他返回值无效
 */
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

        // 识别get请求
        String type = request.getMethod();
        if("GET".equals(type) || url.contains("?")){
            request = new GetRequest(request);
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

        // 拦截空返回
        if(StringUtils.isBlankOrNull(response.getBody())){
            response.setBody(request.getUrl() + " - 返回 null");
        }
    }
}

