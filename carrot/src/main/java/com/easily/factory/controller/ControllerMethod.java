package com.easily.factory.controller;

import com.easily.core.http.Request;
import com.easily.core.http.Response;

public interface ControllerMethod {
    void invoke(Request request, Response response);
}
