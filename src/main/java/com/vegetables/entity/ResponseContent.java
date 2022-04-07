package com.vegetables.entity;

/**
 * 响应体内容
 */
public class ResponseContent {
    private String version;
    private String code;
    private String msg;
    private Object data;

    public ResponseContent() {
        this.setVersion("HTTP/1.1");
        this.setCode("200");
        this.setMsg("OK");
        this.setData("");
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        ResponseContent httpContent = new ResponseContent();
        String responseFirstLine = httpContent.getVersion() + " "
                + httpContent.getCode() + " "
                + httpContent.getMsg() + "\r\n";

        String responseHeader = "Content-Type:" + "html" + "\r\n\r\n";
        String header = responseFirstLine + responseHeader;
        String body = httpContent.getData().toString();
        return header + body;
    }
}
