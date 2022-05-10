package com.easily.factory.controller;

import com.easily.core.http.BaseRequest;
import com.easily.core.http.BaseResponse;
public class ControllerMethodSubstitute {
    private BaseResponse response;
    public ControllerMethod method;

    public BaseResponse get() {
        return response;
    }

    public void get(BaseRequest request,BaseResponse response) {
        this.method.invoke(request, response);
    }

    protected void put(ControllerMethod method) {
        this.method = method;
    }

    protected void put(BaseResponse response) {
        this.response = response;
    }
}
