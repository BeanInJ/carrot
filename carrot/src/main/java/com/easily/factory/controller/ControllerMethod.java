package com.easily.factory.controller;

import com.easily.core.http.BaseRequest;
import com.easily.core.http.BaseResponse;

public interface ControllerMethod {
    void invoke(BaseRequest request, BaseResponse response);
}
