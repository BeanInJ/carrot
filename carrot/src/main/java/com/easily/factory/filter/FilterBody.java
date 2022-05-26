package com.easily.factory.filter;

import com.easily.core.http.Request;
import com.easily.core.http.Response;
import com.easily.factory.aop.MethodBody;

/**
 * 一个FilterBody一个过略器所需要的所有信息
 */
public class FilterBody {
    private Request request;
    private Response response;
    private int Priority = 1000;
    private MethodBody methodBody;

    public MethodBody getMethodBody() {
        return methodBody;
    }

    public void setMethodBody(MethodBody methodBody) {
        this.methodBody = methodBody;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public int getPriority() {
        return Priority;
    }

    public void setPriority(int priority) {
        Priority = priority;
    }
}
