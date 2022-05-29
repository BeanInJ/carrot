package com.easily.core.http;

import com.easily.system.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class Response {
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

    public Response(){
        this.version = "HTTP/1.1";
        this.status = "200";
        this.reason = "OK";

        Map<String, String> map = new HashMap<>();
        map.put("Carrot", "Spicy");
        map.put("Content-Type", "text/html;charset=utf-8");
        map.put("Character-Encoding", "UTF-8");
        this.header = map;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public void setBody(Object body) {
        this.body = StringUtils.toStringOrJson(body);
    }

    public boolean isReturnNow() {
        return isReturnNow;
    }

    public void setReturnNow(boolean returnNow) {
        isReturnNow = returnNow;
    }

    public boolean isGoToController() {
        return isGoToController;
    }

    public void setGoToController(boolean goToController) {
        isGoToController = goToController;
    }

    public void setCookie(String key,String value){
        String cookie = this.header.get("Set-Cookie");
        String kv = key + "=" + value;
        if(StringUtils.isBlank(cookie)){
            this.header.put("Set-Cookie",key+"="+value);
        }else {
            this.header.put("Set-Cookie",cookie + ";" + kv);
        }
    }

    public String toString(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.version).append(" ");
        stringBuffer.append(this.status).append(" ");
        stringBuffer.append(this.reason).append("\r\n");
        // map转为key: value
        this.header.forEach((k,v)->
                stringBuffer.append(k).append(": ").append(v).append("\r\n")
        );

        stringBuffer.append("\r\n");
        if (this.body != null) stringBuffer.append(this.body);
        return stringBuffer.toString();
    }
}
