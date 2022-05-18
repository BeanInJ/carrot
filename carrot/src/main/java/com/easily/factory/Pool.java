package com.easily.factory;

import com.easily.system.util.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

public interface Pool {
    void put(ClassMeta classMeta) throws InvocationTargetException, IllegalAccessException;

    default void filter(ClassMeta classMeta) throws InvocationTargetException, IllegalAccessException {
        if (AnnotationUtils.hasAnnotation(classMeta.getClazz(),getLabel())) put(classMeta);
    }
    Class<? extends Annotation> getLabel();

    void end();
}
