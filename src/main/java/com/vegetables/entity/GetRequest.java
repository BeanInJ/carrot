package com.vegetables.entity;

import com.vegetables.system.exception.CarrotException;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * GET 请求 ， 重点处理url
 */
public class GetRequest extends BaseRequest{

    public GetRequest(BaseHttp baseHttp) throws CarrotException {
        super(baseHttp);
        if(!Objects.equals(this.getMethod(), "GET")){
            throw new CarrotException("非GET请求");
        }
    }

    public GetRequest(ByteBuffer buffer) throws CarrotException {
        super(buffer);
        if(!Objects.equals(this.getMethod(), "GET")){
            throw new CarrotException("非GET请求");
        }
    }

}
