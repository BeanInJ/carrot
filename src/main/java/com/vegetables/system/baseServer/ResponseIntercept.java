package com.vegetables.system.baseServer;

import com.vegetables.annotation.BeforeReturn;
import com.vegetables.entity.HttpGetter;
import com.vegetables.entity.HttpSetter;
import com.vegetables.system.notch.BeforeReturnFunction;

/**
 * Controller方法执行后追加内容
 */
@BeforeReturn
public class ResponseIntercept implements BeforeReturnFunction {
    @Override
    public void beforeReturn(HttpGetter request, HttpSetter httpSetter) {
        httpSetter.setCarrot("Spicy");
        httpSetter.setContentType("text/html;charset=utf-8");
        httpSetter.setCharacterEncoding("UTF-8");

        // 拦截404
        if(httpSetter.getCode().equals("404")){
            httpSetter.setData("404 - Not Found");
        }

        // 拦截空返回
        if(httpSetter.getData() == null || httpSetter.getData().equals("")){
            httpSetter.setData("未返回data");
        }
    }
}
