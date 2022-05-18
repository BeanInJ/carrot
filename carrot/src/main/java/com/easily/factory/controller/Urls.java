package com.easily.factory.controller;

import com.easily.core.http.BaseRequest;
import com.easily.core.http.BaseResponse;
import com.easily.system.util.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Urls {
    private final Map<String, Object[]> urls = new HashMap<>();

    /**
     * 仅传入url，返回空
     */
    public void add(String url) {
        add(url, new BaseResponse());
    }

    /**
     * 传入控制器中的一个方法
     */
    public void add(String url, ControllerMethod method) {
        ControllerMethodSubstitute substitute = new ControllerMethodSubstitute();
        substitute.put(method);
        try {
            Method method1 = substitute.getClass().getMethod("get", BaseRequest.class, BaseResponse.class);
            Object[] objects = {substitute, method1};
            urls.put(url, objects);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 传入需要返回的数据
     */
    public void add(String url, Object response) {
        ControllerMethodSubstitute substitute = new ControllerMethodSubstitute();
        Method method;
        try {
            if (response instanceof BaseResponse) {
                substitute.put((BaseResponse) response);
            } else {
                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setBody(response);
                substitute.put(baseResponse);
            }
            method = substitute.getClass().getMethod("get");
            Object[] objects = {substitute, method};
            urls.put(url, objects);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object[]> getUrls() {
        return urls;
    }
}
