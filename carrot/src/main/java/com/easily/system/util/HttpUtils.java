package com.easily.system.util;


import com.easily.core.http.*;

import java.util.HashMap;
import java.util.Map;

public class HttpUtils {
    /**
     * 获取请求行
     */
    public static String getRequestLine(String request) {
        return request.split("\n")[0];
    }

    /**
     * 获取请求方法（根据请求行获取）
     */
    public static String getRequestMethod(String requestLine) {
        return requestLine.split(" ")[0];
    }

    /**
     * 获取请求路径
     */
    public static String getRequestPath(String requestLine) {
        return requestLine.split(" ")[1];
    }

    /**
     * 获取请求协议
     */
    public static String getRequestProtocol(String request) {
        return getRequestLine(request).split(" ")[2];
    }

    /**
     * 获取请求头
     */
    public static String getRequestHeaders(String request) {
        return request.split("\n\n")[0];
    }

    /**
     * 获取请求体
     */
    public static String getRequestBody(String request) {
        return request.split("\n\n")[1];
    }

    /**
     * 获取请求头的值
     */
    public static String getRequestHeaderValue(String request, String headerName) {
        String headers = getRequestHeaders(request);
        return headers.split(headerName)[1].split(":")[1].trim();
    }

    /**
     * 获取请求体的值
     */
    public static String getRequestBodyValue(String request, String bodyName) {
        String body = getRequestBody(request);
        return body.split(bodyName)[1].split("=")[1].trim();
    }

    /**
     * header转map
     */
    public static Map<String, String> headerToMap(String[] requestLineAndHeader) {
        Map<String, String> map = new HashMap<>();
        // i = 0 是请求行
        for (int i=1; i<requestLineAndHeader.length; i++) {
            String headerOne = requestLineAndHeader[i];
            String headerName = headerOne.split(":")[0].trim();
            String headerValue = headerOne.split(":")[1].trim();
            map.put(headerName, headerValue);
        }
        return map;
    }

    public static Response getTestResponse(){
        Response response =  new Response();
        response.setStatus("200");
        response.setReason("OK");
        response.setVersion("HTTP/1.1");
        response.setBody("{\"code\":\"200\",\"msg\":\"success\",\"data\":{\"id\":\"1\",\"msg\":\"测试BaseResponse\"}}");
        return response;
    }

    public static Request getTestRequest(){
        Request request = new Request();
        request.setBody("{\"id\":\"1\",\"msg\":\"测试BaseRequest\"}");
        request.setUrl("/user/add");
        request.setVersion("HTTP/1.1");
        request.setMethod("POST");
        request.setHeader(new HashMap<>());
        request.addHeaderOne("Content-Type","application/json");
        return request;
    }
}
