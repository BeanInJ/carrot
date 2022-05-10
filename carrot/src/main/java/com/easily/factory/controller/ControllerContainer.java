package com.easily.factory.controller;

import com.easily.core.http.BaseRequest;
import com.easily.core.http.BaseResponse;
import com.easily.factory.ProductContainer;

import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;

/**
 * controller容器，存储 map{url,[类，方法]}
 */
public class ControllerContainer implements ProductContainer {
    private static final Logger log = Logger.getGlobal();

    // map<url,[class，Method]>
    private final Map<String, Object[]> urlMap = new HashMap<>();

    protected void put(String url,Object[] classAndMethod){
        urlMap.put(url,classAndMethod);
    }
    protected void putAll(Map<String, Object[]> map){
        urlMap.putAll(map);
    }

    // 获取url对应的方法
    public Object[] getMethod(String url) {
        if(url.contains("?")){
            url = url.split("\\?")[0];
        }
        return this.urlMap.get(url);
    }

    // 获取controller方法参数
    public Object[] getParams(Method method,BaseRequest request,BaseResponse response){
        List<Object> params = new ArrayList<>();
        for (Class<?> parameterType : method.getParameterTypes()) {
            if (parameterType.equals(BaseRequest.class)) {
                params.add(request);
            } else if (parameterType.equals(BaseResponse.class)) {
                params.add(response);
            } else {
                // 目前前端传过来的全部是String类型，还未进行类型转化
                // 待完善
                params.add(request.getBody());
            }
        }
        return params.toArray();
    }

    public boolean containsKey(String url){
        return urlMap.containsKey(url);
    }

    public void urlMapPrint() {
        urlMap.forEach((k, v) -> {
            String methodName = ((Method)v[1]).getName();
            String className = v[0].getClass().getName();
            String[] ns = className.split("\\.");
            String name = ns[ns.length-1] + "." + methodName;

            log.fine("url: " + k + "  映射方法: " + name);
        });
    }
}
