package com.easily.factory;

import com.easily.core.ConfigCenter;
import com.easily.system.util.AnnotationUtils;
import com.easily.system.util.PackageUtil;
import com.easily.system.util.StringUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class AnnotationScanner extends ClassContainer implements Scanner{
    private static final Logger log = Logger.getGlobal();

    public AnnotationScanner(Set<String> packageNames, Class<? extends Annotation> clazz){
        for (String name:packageNames) getClassByPackageName(name,clazz);
    }

    public AnnotationScanner(String packageName,Class<? extends Annotation> clazz){
        getClassByPackageName(packageName,clazz);
    }

    public void getClassByPackageName(String packageName,Class<? extends Annotation> annotationClazz){
        if(StringUtils.isBlankOrNull(packageName)) return;
        Set<String> classNames;
        try {
            classNames = new HashSet<>(PackageUtil.getClassName(packageName));
        }catch (IOException e){
            e.printStackTrace();
            log.warning("异常包名：" + packageName);
            return;
        }

        for (String className : classNames) {
            try {
                Class<?> aClass = Class.forName(className);
                if(AnnotationUtils.hasAnnotation(aClass, annotationClazz)) {
                    this.put(aClass);
                }
            }catch (ClassNotFoundException e){
                log.info("未扫描到的类型："+className);
            }
        }
    }
}
