package com.vegetables.entity;

/**
 * 响应体内容
 */
public class HttpSetter {
    private String version;
    private String code;
    private String msg;
    private Object data;

    /**
     * 是否放行到Controller
     */
    private boolean isGoToController = true;
    /**
     * 是否立即返回到前端
     */
    private boolean isReturnNow = false;

    public HttpSetter() {
        this.setVersion("HTTP/1.1");
        this.setCode("200");
        this.setMsg("OK");
        this.setData("");
    }

    public boolean isGoToController() {
        return isGoToController;
    }

    public void setGoToController(boolean goToController) {
        isGoToController = goToController;
    }

    public boolean isReturnNow() {
        return isReturnNow;
    }

    public void setReturnNow(boolean returnNow) {
        isReturnNow = returnNow;
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
        if(this.code.equals("404")){
            this.setData("找不到该请求");
        }

        String responseFirstLine = this.getVersion() + " "
                + this.getCode() + " "
                + this.getMsg() + "\r\n";

        String responseHeader = "Content-Type:" + "html" + "\r\n\r\n";
        String header = responseFirstLine + responseHeader;
        String body = this.getData().toString();
        return header + body;
    }
}
