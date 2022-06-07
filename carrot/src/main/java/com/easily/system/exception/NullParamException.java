package com.easily.system.exception;

import java.util.List;

public class NullParamException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private String message;

    private List<String> nullKeys;
    private String code;

    public NullParamException() {
    }

    public NullParamException(String message) {
        super(message);
        this.message = message;
    }

    public NullParamException(List<String> nullKeys) {
        super("含有不能为空的参数");
        this.nullKeys = nullKeys;
    }

    public NullParamException(String message, String code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getNullKeys() {
        return nullKeys;
    }
}
