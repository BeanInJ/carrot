package com.vegetables.system.baseServer;

import com.vegetables.annotation.BeforeEnter;
import com.vegetables.entity.HttpGetter;
import com.vegetables.entity.HttpSetter;
import com.vegetables.system.notch.BeforeEnterFunction;

/**
 * 拦截 /?
 */
@BeforeEnter(0)
public class UrlIntercept implements BeforeEnterFunction {
    public HttpSetter beforeEnterAndReturn(HttpGetter httpGetter){
        String url = httpGetter.getUrl();
        if (url.equals("/?")){
            HttpSetter httpSetter = new HttpSetter();
            httpSetter.setCode("300");
            httpSetter.setMsg("?");
            httpSetter.setData("想做点什么呢？");

            // 直接返回前端，不再执行控制器或后续拦截器
            httpSetter.setReturnNow(true);
            return httpSetter;
        }
        return null;
    }
}
