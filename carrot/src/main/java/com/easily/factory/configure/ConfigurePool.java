package com.easily.factory.configure;

import com.easily.factory.ClassMeta;
import com.easily.factory.Pool;
import com.easily.label.Configure;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class ConfigurePool implements Pool {
    private static final Logger log = Logger.getGlobal();

    @Override
    public void put(ClassMeta classMeta) {
        Class<?> clazz = classMeta.getClazz();
        // 执行clazz中的方法
        for (Method method : clazz.getDeclaredMethods()) {
            try {
                method.invoke(clazz.newInstance());
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Class<? extends Annotation> getLabel() {
        return Configure.class;
    }

    @Override
    public void end() {

    }

}
