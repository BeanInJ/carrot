package com.easily.factory.service;

import com.easily.core.ConfigCenter;
import com.easily.factory.ClassPool;
import com.easily.label.ConfigValue;
import com.easily.label.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.logging.Logger;

public class ServicePool extends ClassPool {
    private static final Logger log = Logger.getGlobal();
    private final ServiceContainer container = new ServiceContainer();

    private final ConfigCenter configCenter;

    public ServicePool(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    @Override
    public Class<? extends Annotation> getLabel() {
        return Service.class;
    }

    @Override
    public String getPoolName() {
        return ServicePool.class.getName();
    }

    @Override
    public void parseToContainer() {
        for (Class<?> clazz : classes) {
            try {
                Object obj = clazz.newInstance();
                putService(obj);
                putConfig(obj);
                if (clazz.getSuperclass() == null) continue; // 判断该对象的父类是否为null
                Class<?>[] interfaces = clazz.getInterfaces();
                if (interfaces.length != 1) continue;
                String serviceName = null;
                for (Class<?> anInterface : interfaces) {
                    serviceName = anInterface.getName();
                }
                this.container.put(serviceName, obj);
            }catch (Exception e){e.printStackTrace();}
        }
    }

    @Override
    public <T> T getProductContainer(Class<T> clazz) {
        return clazz.cast(container);
    }

    /**
     * 注入值
     */
    private void putService(Object obj) throws IllegalAccessException {
        for (Field field : obj.getClass().getDeclaredFields()) {
            if(field.isAnnotationPresent(Service.class)){
                field.setAccessible(true);
                Class<?> type = field.getType();
                Service annotation = field.getAnnotation(Service.class);
                String value = annotation.value();
                if("".equals(value)){
                    // 根据类名获取
                    value = type.getName();
                }
                Object service = this.container.get(value, type);
                field.set(obj, service);
            }
        }
    }

    private void putConfig(Object obj) throws IllegalAccessException {
        for (Field field : obj.getClass().getDeclaredFields()) {
            if(field.isAnnotationPresent(ConfigValue.class)){
                field.setAccessible(true);
                Class<?> type = field.getType();
                ConfigValue annotation = field.getAnnotation(ConfigValue.class);
                String value = annotation.value();
                field.set(obj, this.configCenter.get(value,type));
            }
        }
    }
}
