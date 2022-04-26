package com.carrot.system.exception;

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

//    int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
//    int lineNumber1 = Thread.currentThread().getStackTrace()[1].getLineNumber();
//                System.out.println("第" + lineNumber + "行：" + baseUrl);
//                System.out.println("第" + lineNumber1 + "行：" + baseUrl);
//                throw new CarrotException("\n\tat "+clazz.getName()+ ".<CarrotException>(CarrotException.java:1) @Controller 包含非法路径 ：" + baseUrl);
}
