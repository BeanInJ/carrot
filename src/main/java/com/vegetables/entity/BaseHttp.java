package com.vegetables.entity;

import com.vegetables.system.exception.CarrotException;
import com.vegetables.util.BufferUtils;
import com.vegetables.util.HttpUtils;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * BaseHttp 的设计参考了装饰器设计模式
 * 以BaseHttp为基类的类，内部都应该封装一个baseHttp对象,作为原始数据，一般情况下不对原始数据进行修改
 * <p>
 * Request、Response基类，储存三段：请求行、请求头、请求体
 */
public class BaseHttp implements Http {
    private String[] firstLine;
    private Map<String, String> header;
    private String body;

    public BaseHttp() {
        // 初始化
        this.firstLine = new String[3];
        this.header = new HashMap<>();
        this.body = "";
    }

    public BaseHttp(ByteBuffer buffer) {

        // 解析原始数据
        String requestString = BufferUtils.getString(buffer);
        requestString = requestString.replaceAll("\r", "");
        String[] requestArray = requestString.split("\n\n");
        String[] requestLineAndHeader = requestArray[0].split("\n");

        this.setFirstLine(requestLineAndHeader[0].split(" "));
        this.setHeader(HttpUtils.headerToMap(requestLineAndHeader));

        if (requestArray.length == 1) {
            this.setBody("");
        } else {
            this.setBody(requestArray[1]);
        }
    }

    public String[] getFirstLine() {
        return firstLine;
    }

    public void setFirstLine(String[] firstLine) {
        this.firstLine = firstLine;
    }

    /**
     * 更新第一行
     */
    public void updateFirstLine(String s1, String s2, String s3) {
        // s1、s2、s3 可能是method、url、version （请求）
        // s1、s2、s3 也可能是version、status、reason （响应）
        this.firstLine = new String[]{s1, s2, s3};
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
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
        sb.append(firstLine[0]).append(" ")
                .append(firstLine[1]).append(" ")
                .append(firstLine[2]).append("\r\n");

        for (Map.Entry<String, String> entry : header.entrySet()) {
            sb.append(entry.getKey())
                    .append(":")
                    .append(entry.getValue())
                    .append("\r\n");

        }

        sb.append("\r\n");
        sb.append(body);
        return sb.toString();
    }
}
