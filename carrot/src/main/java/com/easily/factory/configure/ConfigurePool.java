package com.easily.factory.configure;

import com.easily.factory.ClassPool;
import com.easily.label.Configure;
import com.easily.system.dict.INNER;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class ConfigurePool extends ClassPool {
    private static final Logger log = Logger.getGlobal();
    private final ConfigureContainer container = new ConfigureContainer();
    @Override
    public Class<? extends Annotation> getLabel() {
        return Configure.class;
    }

    @Override
    public String getPoolName() {
        return INNER.CONFIGURE_POOl_NAME;
    }

    @Override
    public void parseToContainer() {
        for(Class<?> clazz:classes){
            // 执行clazz中的方法
            for (Method method : clazz.getDeclaredMethods()) {
                try {
                    method.invoke(clazz.newInstance());
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public <T> T getProductContainer(Class<T> clazz) {
        return clazz.isAssignableFrom (ConfigureContainer.class)? clazz.cast(this.container):null;
    }
}
