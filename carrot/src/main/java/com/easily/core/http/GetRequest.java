package com.easily.core.http;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * GET 请求 ， 重点处理url
 */
public class GetRequest extends BaseRequest{
    private final Map<String,String> urlParam = new HashMap<>();

    /**
     * 将BaseRequest（父类）转为GetRequest（子类）
     */
    public GetRequest(BaseRequest request){
        super();

        if(Objects.equals(request.getMethod(), "GET")){
            initUrlParam(request.getUrl());
        }

        this.setBaseHttp(request.getBaseHttp());

        this.setVersion(request.getVersion());
        this.setHeader(request.getHeader());
        this.setBody(request.getBody());
    }

    public GetRequest(BaseHttp baseHttp){
        super(baseHttp);
        if(Objects.equals(this.getMethod(), "GET")){
            initUrlParam(getUrl());
        }
    }

    public GetRequest(ByteBuffer buffer){
        super(buffer);
        if(Objects.equals(this.getMethod(), "GET")){
            initUrlParam(getUrl());
        }
    }

    /**
     * 获取url中的参数列表
     */
    public Map<String, String> getParams() {
        return urlParam;
    }

    /**
     * 获取url中的某个参数值
     */
    public String getParam(String key) {
        return urlParam.get(key);
    }

    /**
     * 放入一个值
     */
    public void setParam(String key,String value) {
        urlParam.put(key,value);
    }


    /**
     * 初始化url中的参数
     */
    private void initUrlParam(String urlLong){
        String[] urlArray = urlLong.split("\\?");
        this.setUrl(urlArray[0]);
        if(urlArray.length > 1){
            String paramString = urlLong.replace(urlArray[0]+"?", "");
            for (String param : paramString.split("&")) {
                String[] kv = param.split("=");
                if(kv.length > 1){
                    urlParam.put(kv[0],kv[1]);
                }else {
                    urlParam.put(kv[0],null);
                }
            }
        }
    }

}
