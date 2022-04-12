package com.vegetables.entity;

import com.google.gson.Gson;
import com.vegetables.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回json格式的响应体
 */
public class JsonResponse extends BaseResponse {
    private Map<String,Object> jsonBody = new HashMap<>();

    public JsonResponse() {
        this.addHeaderOne("Content-Type", "application/json");
        String body = this.getBody();
        if (StringUtils.isNotBlankOrNull(body)) {
            this.jsonBody = (new Gson()).fromJson(body, Map.class);
        }
    }

    public JsonResponse(BaseHttp baseHttp) {
        super(baseHttp);
    }

    public Object getParam(String name) {
        return this.jsonBody.get(name);
    }

    public void addParam(String name, Object value) {
        this.jsonBody.put(name, value);
    }

    public String getJsonBody() {
        return (new Gson()).toJson(this.jsonBody);
    }

    public String toString() {
        this.setBody(this.getJsonBody());
        return super.toString();
    }

    public static void main(String[] args) {
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.addParam("name", "zhangsan");
        jsonResponse.addParam("age", "18");
        System.out.println(jsonResponse.toString());
    }
}
