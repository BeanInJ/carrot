package com.easily.factory.controller;

import com.easily.core.http.Request;
import com.easily.core.http.Response;
public class ControllerMethodSubstitute {
    private Response response;
    public ControllerMethod method;

    public Response get() {
        return response;
    }

    public void get(Request request,Response response) {
        this.method.invoke(request, response);
    }

    protected void put(ControllerMethod method) {
        this.method = method;
    }

    protected void put(Response response) {
        this.response = response;
    }
}
