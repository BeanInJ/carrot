package com.easily.factory.controller;

import com.easily.core.http.BaseRequest;
import com.easily.core.http.BaseResponse;
import com.easily.system.util.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Urls {
    private final Map<String, Object[]> urls = new HashMap<>();

    public void add(String url) {
        add(url, new BaseResponse());
    }

    public void add(String url, Object response) {
        ControllerMethodSubstitute substitute = new ControllerMethodSubstitute();
        Method method = null;
        try {
            if (response instanceof ControllerMethod) {
                substitute.put((ControllerMethod) response);
                method = substitute.getClass().getMethod("get", BaseRequest.class, BaseResponse.class);
            } else if (response instanceof BaseResponse) {
                substitute.put((BaseResponse) response);
                method = substitute.getClass().getMethod("get");
            } else {
                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setBody(StringUtils.toStringOrJson(response));
                substitute.put(baseResponse);
                method = substitute.getClass().getMethod("get");
            }
        } catch (NoSuchMethodException ignored) {
        }

        Object[] objects = {substitute, method};
        urls.put(url, objects);

    }

    protected Map<String, Object[]> getUrls() {
        return urls;
    }
}
