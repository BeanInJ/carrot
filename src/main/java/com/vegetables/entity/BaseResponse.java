package com.vegetables.entity;

import com.vegetables.util.MapUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 响应内容
 *
 * 数据处理逻辑：BaseResponse入数据 -> 封装为BaseHttp原始数据 -> 前端字符流
 * 响应的设计 入数据需求 > 取数据需求
 */
public class BaseResponse implements Http{
    private final BaseHttp baseHttp;
    private String version;
    private String status;
    private String reason;
    private Map<String,String> header;
    private String body;

    private boolean isReturnNow = false;
    private boolean isGoToController = true;

    public BaseResponse() {
        this.baseHttp = new BaseHttp();
        this.version = "HTTP/1.1";
        this.status = "200";
        this.reason = "OK";
        Map<String, String> map = new HashMap();
        map.put("Carrot", "Spicy");
        map.put("Content-Type", "text/html;charset=utf-8");
        map.put("Character-Encoding", "UTF-8");
        this.header = map;
    }

    /**
     * 从封装好的原始数据中刷新响应内容
     */
    public BaseResponse(BaseHttp baseHttp) {
        this.baseHttp = baseHttp;
        this.refresh();
    }

    public BaseHttp getBaseHttp() {
        BaseHttp baseHttp = new BaseHttp();
        baseHttp.setFirstLine(this.getFirstLine());
        baseHttp.setHeader(MapUtils.copyMap(this.getHeader()));
        baseHttp.setBody(this.getBody());
        return baseHttp;
    }

    public boolean isGoToController() {
        return isGoToController;
    }

    public void setIsGoToController(boolean isGoToController) {
        this.isGoToController = isGoToController;
    }

    public boolean isReturnNow() {
        return isReturnNow;
    }

    public void setReturnNow(boolean returnNow) {
        isReturnNow = returnNow;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Map<String, String> getHeader() {
        return this.header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public void addHeaderOne(String name, String value) {
        this.header.put(name, value);
    }
    public String getHeaderOne(String name) {
        return this.header.get(name);
    }

    public String getBody() {
        return this.body;
    }

    public void setFirstLine(String[] firstLine) {
        this.version = firstLine[0];
        this.status = firstLine[1];
        this.reason = firstLine[2];
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String[] getFirstLine() {
        return new String[]{this.version, this.status, this.reason};
    }

    /**
     * 从原始对象baseHttp中刷新BaseResponse
     */
    public void refresh() {
        if (this.baseHttp != null) {
            this.version = this.baseHttp.getFirstLine()[0];
            this.status = this.baseHttp.getFirstLine()[1];
            this.reason = this.baseHttp.getFirstLine()[2];
            this.header = MapUtils.copyMap(this.baseHttp.getHeader());
            this.body = this.baseHttp.getBody();
        }
    }

    public String toString() {
        return this.getBaseHttp().toString();
    }
}
