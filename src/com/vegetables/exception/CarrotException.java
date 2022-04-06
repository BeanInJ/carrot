package com.vegetables.exception;

public class CarrotException extends Exception{
    private static final long serialVersionUID = 1L;
    private String message;
    private String code;

    public CarrotException() {
    }

    public CarrotException(String message) {
        super(message);
        this.message = message;
    }

    public CarrotException(String message, String code) {
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
}
