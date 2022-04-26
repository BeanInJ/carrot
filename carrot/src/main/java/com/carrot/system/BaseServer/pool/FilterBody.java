package com.carrot.system.BaseServer.pool;

import com.carrot.aop.MethodBody;
import com.carrot.core.http.BaseRequest;
import com.carrot.core.http.BaseResponse;

/**
 * 一个FilterBody一个过略器所需要的所有信息
 */
public class FilterBody {
    private BaseRequest request;
    private BaseResponse response;
    private int Priority = 1000;
    private MethodBody methodBody;

    public MethodBody getMethodBody() {
        return methodBody;
    }

    public void setMethodBody(MethodBody methodBody) {
        this.methodBody = methodBody;
    }

    public BaseRequest getRequest() {
        return request;
    }

    public void setRequest(BaseRequest request) {
        this.request = request;
    }

    public BaseResponse getResponse() {
        return response;
    }

    public void setResponse(BaseResponse response) {
        this.response = response;
    }

    public int getPriority() {
        return Priority;
    }

    public void setPriority(int priority) {
        Priority = priority;
    }
}
