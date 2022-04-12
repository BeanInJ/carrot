package com.vegetables.entity;

import com.vegetables.system.exception.CarrotException;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * post请求
 */
public class PostRequest extends BaseRequest{

    public PostRequest(BaseHttp baseHttp) throws CarrotException {
        super(baseHttp);
        if(Objects.equals(this.getMethod(), "GET")){
            throw new CarrotException("非POST请求");
        }
    }

    public PostRequest(ByteBuffer buffer) throws CarrotException {
        super(buffer);
        if(Objects.equals(this.getMethod(), "GET")){
            throw new CarrotException("非POST请求");
        }
    }

}
