package com.carrot.factory.engine;

/**
 * InvokeBody 在某个方法确认为目标方法后 new
 */
public class InvokeBody {
    /**
     * 目标方法需要的参数
     */
    private Object[] params;
    /**
     * 目标方法方法的返回结果
     */
    private Object returnValue;
    /**
     * 功能方法体ID
     */
    private String accessMethodId;

    public String getAccessMethodId() {
        return accessMethodId;
    }

    public void setAccessMethodId(String accessMethodId) {
        this.accessMethodId = accessMethodId;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }
}
