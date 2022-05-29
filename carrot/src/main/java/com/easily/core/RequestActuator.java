package com.easily.core;

import com.easily.core.http.HttpReader;
import com.easily.core.http.Request;
import com.easily.core.http.Response;
import com.easily.factory.aop.AopMethod;
import com.easily.factory.aop.AopPool;
import com.easily.factory.controller.ControllerPool;
import com.easily.factory.filter.FilterPool;
import com.easily.label.Aop;
import com.easily.label.Controller;
import com.easily.label.Filter;
import com.easily.system.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class RequestActuator{
    private static final Logger log = Logger.getGlobal();
    private Request request;
    private Response response;

    private final DataSwap dataSwap;

    public RequestActuator(DataSwap dataSwap) {
        this.dataSwap = dataSwap;
    }

    /**
     * 执行请求处理流程
     */
    public void flow() throws IOException {
        try {
            // 获取请求->前过滤->控制器->后过滤

            // true 继续执行
            // false 跳出
            boolean flow = this.getRequest() && this.passBeforeFilter() && this.passController() && this.passAfterFilter();
        } catch (Exception e) {
            e.printStackTrace();
            // 异常拦截
            this.ifException();
        }
        this.putResponse();
    }

    /**
     * 获取请求体
     */
    private boolean getRequest(){
        boolean isHttpRequest = dataSwap.httpReader.getHttp();
        if (isHttpRequest) {
            this.request = new Request();
            this.request.setMethod(dataSwap.httpReader.getMethod());
            this.request.setUrl(dataSwap.httpReader.getUrl());
            this.request.setVersion(dataSwap.httpReader.getVersion());
            this.request.setHeader(dataSwap.httpReader.getHeaders());
            this.request.setBody(dataSwap.httpReader.getStringBody());
        }
        return isHttpRequest;
    }

    /**
     * 通过前置过略器
     */
    private boolean passBeforeFilter() {
        FilterPool filterPool = dataSwap.pools.get(Filter.class, FilterPool.class);
        this.response = filterPool.beforeController(this.request);

        // 如果经过过略器后，response还未初始化，则在此初始化
        if (this.response == null) {
            this.response = new Response();
        }
        return !this.response.isReturnNow();
    }

    /**
     * 通过控制器
     */
    private boolean passController() {
        if (this.response.isGoToController()) {
            // 获取url对应的方法
            ControllerPool controllerPool = dataSwap.pools.get(Controller.class, ControllerPool.class);
            Object[] classAndMethod = controllerPool.getMethod(request.getUrl());

            if (classAndMethod == null) {
                this.response.setStatus("404");
                return true;
            }

            // 方法、类型、参数
            Method method = (Method) classAndMethod[1];
            Object object = classAndMethod[0];
            Object[] params = controllerPool.assemblyParams(method, this.request, this.response);

            // 获取切面信息
            AopPool aopPool = dataSwap.pools.get(Aop.class, AopPool.class);
            AopMethod aopMethod = null;
            try {
                aopMethod = aopPool.getAopMethod(method, object);
                Object returnValue;
                if (aopMethod == null) {
                    // 切面信息为空，则直接执行方法
                    returnValue = method.invoke(object, params);
                } else {
                    // 包切面信息，则执行切面方法
                    returnValue = aopMethod.invoke(params);
                }

                // 将返回值设置到response中
                if (returnValue instanceof Response) {
                    this.response = (Response) returnValue;
                } else if (StringUtils.isNotBlankOrNull(returnValue)) {
                    this.response.setBody(returnValue);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 通过后置过滤器
     */
    private boolean passAfterFilter() {
        FilterPool filterPool = dataSwap.pools.get(Filter.class, FilterPool.class);
        filterPool.afterController(this.request, this.response);
        return !this.response.isReturnNow();
    }

    /**
     * 执行异常拦截器
     */
    private void ifException() {
    }

    private boolean putResponse(){
        if(this.response == null){
            return false;
        }else  {
            byte[] bytes = this.response.toString().getBytes(StandardCharsets.UTF_8);
            dataSwap.Response = ByteBuffer.wrap(bytes);
            return true;
        }
    }
}
