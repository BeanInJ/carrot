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
    }
}
