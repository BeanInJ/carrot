package com.easily.system.util;

import javax.annotation.*;
import java.lang.annotation.*;

public class AnnotationUtils {
    // 递归判断是否有某个注解
    public static boolean hasAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        for (Annotation annotation : clazz.getAnnotations()) {
            if(annotation.annotationType() == annotationClass) return true;
            if(isContinue(annotation)) continue;
            if(hasAnnotation(annotation.annotationType(),annotationClass)) return true;
        }
        return false;
    }

    public static boolean isResourceAnnotation(Class<? extends Annotation> subAnnotation){
        return subAnnotation.isAnnotationPresent(Resource.class);
    }

    // 元注解不判断
    public static boolean isContinue(Annotation annotation){
        return annotation.annotationType() == Deprecated.class ||
                annotation.annotationType() == SuppressWarnings.class ||
                annotation.annotationType() == Override.class ||
                annotation.annotationType() == PostConstruct.class ||
                annotation.annotationType() == PreDestroy.class ||
                annotation.annotationType() == Resource.class ||
                annotation.annotationType() == Resources.class ||
                annotation.annotationType() == Generated.class ||
                annotation.annotationType() == Target.class ||
                annotation.annotationType() == Retention.class ||
                annotation.annotationType() == Documented.class ||
                annotation.annotationType() == Inherited.class;
    }
}
