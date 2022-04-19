package com.carrot.core.http;

import com.carrot.system.util.MapUtils;
import com.carrot.system.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 响应内容基类
 *
 * 数据处理逻辑：BaseResponse入数据 -> 封装为BaseHttp原始数据 -> 前端字符流
 * 响应的设计 入数据需求 > 取数据需求
 */
public class BaseResponse implements Http{
    private static final Logger log = Logger.getGlobal();

    private BaseHttp baseHttp;
    private String version;
    private String status;
    private String reason;
    private Map<String,String> header;
    private String body;

    /**
     * 立即返回，不再通过其他过滤器方法，不再通过控制器方法
     */
    private boolean isReturnNow = false;
    /**
     * 直接跳到控制器方法，不再执行其他过滤器方法
     */
    private boolean isGoToController = true;

    /**
     * 初始化一个新响应实体
     */
    public BaseResponse() {
        this.baseHttp = new BaseHttp();
        initResponse();
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
     * 初始化响应内容
     */
    public void initResponse() {
        if(this.version == null) {
            this.version = "HTTP/1.1";
        }
        if(this.status == null) {
            this.status = "200";
        }
        if(this.reason == null) {
            this.reason = "OK";
        }

        if(this.header == null || this.header.size() == 0) {
            Map<String, String> map = new HashMap<>();
            map.put("Carrot", "Spicy");
            map.put("Content-Type", "text/html;charset=utf-8");
            map.put("Character-Encoding", "UTF-8");
            this.header = map;
        }
    }

    /**
     * 设置cookie
     */
    public void setCookie(String key,String value) {
        String cookie = this.header.get("Set-Cookie");
        String kv = key + "=" + value;
        if(StringUtils.isBlank(cookie)){
            this.header.put("Set-Cookie",key+"="+value);
        }else {
            this.header.put("Set-Cookie",cookie + ";" + kv);
        }
    }

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

        initResponse();
    }

    public String toString() {
        return this.getBaseHttp().toString();
    }
}
