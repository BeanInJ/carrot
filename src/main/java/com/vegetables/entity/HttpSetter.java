package com.vegetables.entity;

/**
 * 响应体内容
 */
public class HttpSetter {
    private String version;
    private String code;
    private String msg;
    private Object data;

    private String carrot;
    private String contentType;
    private String characterEncoding;

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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    public String getCarrot() {
        return carrot;
    }

    public void setCarrot(String carrot) {
        this.carrot = carrot;
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

        StringBuilder stringBuffer =new StringBuilder();
        stringBuffer.append(this.getVersion()).append(" ");
        stringBuffer.append(this.getCode()).append(" ");
        stringBuffer.append(this.getMsg()).append("\r\n");

        if(this.carrot != null){
            stringBuffer.append("Carrot:").append(this.carrot).append("\r\n");
        }
        if (this.contentType != null){
            stringBuffer.append("Content-Type:").append(this.contentType).append("\r\n");
        }
        if(this.characterEncoding != null){
            stringBuffer.append("Character-Encoding:").append(this.characterEncoding).append("\r\n");
        }

        stringBuffer.append("\r\n");

        stringBuffer.append(this.getData().toString());

        return stringBuffer.toString();
    }
}
