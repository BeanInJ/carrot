package com.easily.factory;

import java.util.Map;

/**
 * class元信息
 */
public class ClassMeta {
    /**
     * id
     */
    private int id;
    /**
     * class名称
     */
    private String name;
    /**
     * class
     */
    private Class<?> clazz;
    /**
     * 原始参数
     */
    private Map<String,Object[]> metaPramMap;

    /**
     * 根据原始参数初始化的对象
     */
    private Object newObject;

    /**
     * 属于哪种类型的class
     */
    private Class<? extends Pool> poolType;

    public ClassMeta(String name,Class<?> clazz){
        this.name = name;
        this.clazz = clazz;
    }

    public ClassMeta(Class<?> clazz){
        this.name = clazz.getName();
        this.clazz = clazz;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Map<String, Object[]> getMetaPramMap() {
        return metaPramMap;
    }

    public void setMetaPramMap(Map<String, Object[]> metaPramMap) {
        this.metaPramMap = metaPramMap;
    }

    public Object getNewObject() {
        return newObject;
    }

    public void setNewObject(Object newObject) {
        this.newObject = newObject;
    }

    public Class<? extends Pool> getPoolType() {
        return poolType;
    }

    public void setPoolType(Class<? extends Pool> poolType) {
        this.poolType = poolType;
    }
}
