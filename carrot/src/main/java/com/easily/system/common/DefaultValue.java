package com.easily.system.common;

public class DefaultValue<T> {
    private final T t;
    public DefaultValue(T t){
        this.t = t;
    }
    public T get(){
        return this.t;
    }
}
