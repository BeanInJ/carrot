package com.vegetables.entity;

import com.vegetables.util.MapUtils;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 请求内容基类
 *
 * 数据处理逻辑： 前端字符流 -> BaseHttp原始数据 -> BaseRequest加工处理，方便获取参数
 * 请求的设计 取数据需求 > 入数据需求
 */
public class BaseRequest implements Http {
    private static final Logger log = Logger.getGlobal();

    private BaseHttp baseHttp;
    private String url;
    private String method;
    private String version;
    private Map<String,String> header;

    public BaseRequest() {
    }

    public BaseRequest(BaseHttp baseHttp) {
        this.baseHttp = baseHttp;
        this.refresh();
    }

    public BaseRequest(ByteBuffer buffer){
        this.baseHttp = new BaseHttp(buffer);
        this.refresh();
    }

    public void reload(ByteBuffer buffer){
        this.baseHttp = new BaseHttp(buffer);
        this.refresh();
    }

    public BaseHttp getBaseHttp() { return this.baseHttp; }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public void setBody(String body) {
    }

    public Map<String, String> getHeader() {
        return this.header;
    }

    public void addHeaderOne(String key, String value) {
        this.header.put(key, value);
    }

    public String getHeaderOne(String key) {
        return this.header.get(key);
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setFirstLine(String method, String url, String version) {
        this.method = method;
        this.url = url;
        this.version = version;
    }

    public String[] getFirstLine() {
        return new String[]{this.method, this.url, this.version};
    }

    public String getBody() {
        return this.baseHttp.getBody();
    }

    public void setFirstLine(String[] firstLine) {
        this.setMethod(firstLine[0]);
        this.setUrl(firstLine[1]);
        this.setVersion(firstLine[2]);
    }

    /**
     * 从原始数据中刷新
     */
    public void refresh() {
        if (this.baseHttp != null) {
            this.method = this.baseHttp.getFirstLine()[0];
            this.url = this.baseHttp.getFirstLine()[1];
            this.version = this.baseHttp.getFirstLine()[2];
            this.header = MapUtils.copyMap(this.baseHttp.getHeader());
        }
    }

    /**
     * 获取请求体中的cookie
     */
    public String getCookie(String key){
        String cookie = this.header.get("Set-Cookie");
        // 解析cookie中的键值对
        String[] kvs = cookie.split(";");
        for(String kv : kvs) {
            try {
                String[] kvArr = kv.split("=");
                if (kvArr[0].equals(key)) {
                    return kvArr[1];
                }
            }catch (Exception e) {
                e.printStackTrace();
                log.fine("解析cookie失败，失败键值对：" + kv);
                return null;
            }
        }
        return null;
    }

    public String toString() {
        BaseHttp baseHttp = new BaseHttp();
        baseHttp.setFirstLine(this.getFirstLine());
        baseHttp.setHeader(this.getHeader());
        baseHttp.setBody(this.getBody());
        return baseHttp.toString();
    }
}
