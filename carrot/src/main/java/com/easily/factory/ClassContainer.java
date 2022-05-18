package com.easily.factory;

import com.easily.core.ConfigCenter;
import com.easily.label.ConfigValue;
import com.easily.label.Service;
import com.easily.system.common.Getter;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class ClassContainer implements Getter<String, ClassMeta> {
    private static final Logger log = Logger.getGlobal();
    private final List<ClassMeta> classMetaList = new ArrayList<>();

    private ConfigCenter configCenter;

    @Override
    public ClassMeta get(String name) {
        for (ClassMeta classMeta : classMetaList) {
            if (Objects.equals(classMeta.getName(), name)) return classMeta;
        }
        return null;
    }

    /**
     * 放入单个
     */
    public boolean put(String name, Class<?> clazz) {
        repeatThrow(name);
        return classMetaList.add(new ClassMeta(name, clazz));
    }

    /**
     * 放入单个
     */
    public boolean put(Class<?> clazz) {
        // 一个service实现类的name是对应service接口的全名
        String name = null;
        if (clazz.isAnnotationPresent(Service.class)) {
            // service实现类：有service注解、继承自某一个接口
            if (clazz.getSuperclass() != null) {
                Class<?>[] interfaces = clazz.getInterfaces();
                if (interfaces.length == 1) {
                    for (Class<?> anInterface : interfaces) {
                        name = anInterface.getName();
                    }
                }
            }
        }

        if (name == null) {
            name = clazz.getName();
        }

        repeatThrow(name);
        return classMetaList.add(new ClassMeta(clazz));
    }

    /**
     * 放入重复clazz抛出异常
     */
    private void repeatThrow(String name) {
        for (ClassMeta classMeta : classMetaList) {
            if (classMeta.getName().equals(name))
                throw new RuntimeException("重复ClassName " + name);
        }
    }

    public List<ClassMeta> getAll() {
        return this.classMetaList;
    }

    /**
     * 获取已经初始化的对象
     */
    public Object getObject(String name) {
        for (ClassMeta classMeta : classMetaList) {
            if (classMeta.getName().equals(name)) return classMeta.getNewObject();
        }
        return null;
    }

    /**
     * 统一初始化
     */
    public void unifyInit() throws IllegalAccessException, InstantiationException {
        // 第一遍，初始化
        for (ClassMeta classMeta : classMetaList) {
            Class<?> clazz = classMeta.getClazz();
            Object o = clazz.newInstance();
            classMeta.setNewObject(o);
        }

        // 第二遍，向对象中注入参数（需要注入的参数来自 classMetaList 中）
        for (ClassMeta classMeta : classMetaList) {
            Object object = classMeta.getNewObject();
            if (object == null) continue;

            // 循环所有字段，找到注入值
            for (Field field : object.getClass().getDeclaredFields()) {
                // 判断是有继承自Resource的注解
                Annotation resourceAnnotation = getResourceAnnotation(field);
                if (resourceAnnotation == null) continue;

                // 如果有，获取需要注入的类名
                field.setAccessible(true);

                if(field.isAnnotationPresent(ConfigValue.class)){
                    putConfigValue(object,field);
                }else {
                    putContainerValue(object,field,resourceAnnotation);
                }
            }
        }

    }

    /**
     * 判断字段是否有Resource注解
     */
    private Annotation getResourceAnnotation(Field field) {
        for (Annotation annotation : field.getAnnotations()) {
            Class<? extends Annotation> annotationClass = annotation.getClass();
            if (annotation instanceof Resource || annotationClass.isAnnotationPresent(Resource.class)) {
                return annotation;
            }
        }
        return null;
    }

    /**
     * 获取注解的value
     */
    private String getAnnotationValue(Annotation annotation) {
        try {
            Class<? extends Annotation> annotationClass = annotation.getClass();
            Method method = annotationClass.getMethod("value");
            return (String) method.invoke(annotation);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    /**
     * 注入来自配置中心的值
     */
    private void putConfigValue(Object obj,Field field) throws IllegalAccessException {
        Class<?> type = field.getType();
        ConfigValue annotation = field.getAnnotation(ConfigValue.class);
        String value = annotation.value();
        field.set(obj, this.configCenter.get(value, type));
    }

    private void putContainerValue(Object object,Field field,Annotation resourceAnnotation) throws IllegalAccessException {
        String className = getAnnotationValue(resourceAnnotation);
        Class<?> type = field.getType();
        if (className == null) {
            className = type.getName();
        }

        // 获取类名对应的对象
        Object objectValue = getObject(className);

        // 注入
        field.set(object, objectValue);
    }

    public void configCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }
}
