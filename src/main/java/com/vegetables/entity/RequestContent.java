package com.vegetables.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求体内容
 */
public class RequestContent {
    private String url;
    private String method;
    private String version;
    private Map<String, String> headers;
    private String body;

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public RequestContent() {
    }

    public RequestContent(String url, String method, String body) {
        this.url = url;
        this.method = method;
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "HttpContent{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", version='" + version + '\'' +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }

    public static RequestContent init(String content) {
        // 这里有多线程抢资源问题，需要优化
        System.out.println("线程id："+Thread.currentThread().getId());

        RequestContent httpContent = new RequestContent();
        String[] strings = content.split("\r\n\r\n");
        String[] headerAndRequestLine = strings[0].split("\r\n");
        String[] requestLine = headerAndRequestLine[0].split(" ");

        // 获取请求方法
        httpContent.setMethod(requestLine[0]);
        // 获取接口
        httpContent.setUrl(requestLine[1]);
        // 获取版本
        httpContent.setVersion(requestLine[2]);

        if(strings.length > 1){
            // body
            httpContent.setBody(strings[1]);
        }

        Map<String, String> header = new HashMap<>();
        for (int i = 1; i < headerAndRequestLine.length; i++) {
            String key = headerAndRequestLine[i].split(":")[0].trim();
            String value = headerAndRequestLine[i].split(":")[1].trim();
            header.put(key, value);
        }
        httpContent.setHeaders(header);
        return httpContent;
    }
}
