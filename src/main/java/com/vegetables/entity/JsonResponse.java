package com.vegetables.entity;

import com.google.gson.Gson;
import com.vegetables.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回json格式的响应体
 *
 *
 */
public class JsonResponse extends BaseResponse {
    private Map<String,Object> jsonBody = new HashMap<>();

    // 新构造一个json响应体
    public JsonResponse() {
        super();
        String body = this.getBody();
        initJsonResponse(body);
    }

    // 将一个http消息体基类转为json相应类
    public JsonResponse(BaseHttp baseHttp) {
        super(baseHttp);
        String body = this.getBody();
        initJsonResponse(body);
    }

    // 将一个基础响应类转为json相应类
    public JsonResponse(BaseResponse baseResponse) {
        this.setVersion(baseResponse.getVersion());
        this.setStatus(baseResponse.getStatus());
        this.setReason(baseResponse.getReason());
        this.setHeader(baseResponse.getHeader());
        String body = baseResponse.getBody();
        initJsonResponse(body);
    }

    // 放入一个实体类转为jsonMap
    public <T> void putClassBody(T body) {
        String s = (new Gson()).toJson(body);
        Map<String,Object> map = (new Gson()).fromJson(s, Map.class);
        this.jsonBody.putAll(map);
    }

    // 获取body中一个参数值
    public Object getParam(String name) {
        return this.jsonBody.get(name);
    }

    // 往body中添加一个键值对
    public void addParam(String name, Object value) {
        this.jsonBody.put(name, value);
    }

    // 获取json body
    public String getJsonBody() {
        return (new Gson()).toJson(this.jsonBody);
    }

    // 获取json响应体（字符串）
    public String toString() {
        this.setBody(this.getJsonBody());
        return super.toString();
    }

    // 初始化json响应体
    private void initJsonResponse(String body){
        this.addHeaderOne("Content-Type", "application/json");
        if(StringUtils.isNotBlankOrNull(body)){
            if(body.startsWith("{") && body.endsWith("}")){
                // 把string转为map
                this.jsonBody = (new Gson()).fromJson(body, Map.class);
            }
        }
    }
}