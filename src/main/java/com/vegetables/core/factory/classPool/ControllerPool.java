package com.vegetables.core.factory.classPool;

import com.vegetables.annotation.Controller;

import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Logger;

/**
 * Controller 控制器类池
 */
public class ControllerPool{
    private static final Logger log = Logger.getGlobal();

    private static final ClassPoolCore CLASS_POOL_CORE = new ClassPoolCore();

    public static void trim() {
        for(Class<?> clazz:getClasses()){
            for (Method method : clazz.getMethods()) {
                if(method.isAnnotationPresent(Controller.class)){
                    break;
                }
                log.info("移除无效控制器：" + clazz.getName());
                CLASS_POOL_CORE.remove(clazz);
            }
        }
    }


    public static void add(Class<?> clazz) {
        CLASS_POOL_CORE.add(clazz);
    }

    public static List<Class<?>> getClasses() {
        return CLASS_POOL_CORE.getClasses();
    }

    public static void setClasses(List<Class<?>> classes) {
        CLASS_POOL_CORE.setClasses(classes);
    }
}
