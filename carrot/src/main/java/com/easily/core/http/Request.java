package com.easily.core.http;

import java.util.Map;

public class Request {
    // 请求方法
    private String method;
    // 请求Url
    private String url;
    // HTTP协议版本
    private String version;
    // 请求头
    private Map<String,String> header;
    // body
    private String body;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public void addHeaderOne(String key,String value) {
        this.header.put(key, value);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.method).append(" ");
        sb.append(this.url).append(" ");
        sb.append(this.version).append("\r\n");

        for (Map.Entry<String, String> entry : this.header.entrySet()) {
            sb.append(entry.getKey()).append(": ");
            sb.append(entry.getValue()).append("\r\n");
        }

        sb.append("\r\n");
        sb.append(this.body);
        return sb.toString();
    }
}
