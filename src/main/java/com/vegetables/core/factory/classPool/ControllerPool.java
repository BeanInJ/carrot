package com.vegetables.core.factory.classPool;

import com.vegetables.label.annotation.Controller;
import com.vegetables.core.factory.Pool;
import com.vegetables.system.dict.Msg;
import com.vegetables.util.UrlUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Controller 控制器类池
 */
public class ControllerPool implements Pool {
    private static final Logger log = Logger.getGlobal();

    private static final ClassPoolCore CLASS_POOL_CORE = new ClassPoolCore();

    public void trim() {
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

    @Override
    public void add(Object o) {
        CLASS_POOL_CORE.add((Class<?>)o);
    }

    /**
     * 分发给Url控制器
     */
    public static void dispense(Map<String, Object[]> urlMap){
        for (Class<?> clazz : getClasses()) {
            // 获取类名上的url
            Controller controllerInClass = clazz.getAnnotation(Controller.class);
            // 创建对象
            Object newClazz;
            try {
                newClazz = clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            String baseUrl = UrlUtils.correctUri(controllerInClass.value());

            // 获取方法上的url
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (method.isAnnotationPresent(Controller.class)) {
                    Controller controllerInMethod = method.getAnnotation(Controller.class);
                    String url = UrlUtils.correctUri(controllerInMethod.value());
                    if (!baseUrl.equals("/")) {
                        url = baseUrl + url;
                    }

                    // 不能放入map条件：map中已有，并且不能覆盖
                    if (urlMap.containsKey(url)) {
                        if (controllerInMethod.isCover()) {
                            // 覆盖
                            Object oldClass = urlMap.get(url)[0];
                            Method oldMethod = (Method) urlMap.get(url)[1];
                            log.info("url: " + url +
                                    Msg.ERROR_URL_DUPLICATE +
                                    oldClass.getClass().getName() + "." + oldMethod.getName());
                        } else {
                            // 不能覆盖
                            log.info("url: " + url +
                                    Msg.ERROR_URL_DUPLICATE +
                                    clazz.getName() + "." + method.getName());
                            continue;
                        }
                    }
                    urlMap.put(url, new Object[]{newClazz, method});
                }
            }
        }
    }
}
