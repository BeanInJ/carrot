package com.vegetables.core;

import com.vegetables.annotation.Controller;
import com.vegetables.dict.Msg;
import com.vegetables.system.exception.CarrotException;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 将路由映射到对应的方法上
 */
public class UrlDistribute {
    private static final Logger log = Logger.getGlobal();
    private static final Map<String, Object[]> urlMap = new HashMap<>();

    public UrlDistribute(List<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            // 获取类名上的url
            Controller controllerInClass = clazz.getAnnotation(Controller.class);
            // 创建对象
            Object newClazz = null;
            try {
                newClazz = clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String baseUrl = correctUrl(controllerInClass.value());

            // 获取方法上的url
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (method.isAnnotationPresent(Controller.class)) {
                    Controller controllerInMethod = method.getAnnotation(Controller.class);
                    String url = correctUrl(controllerInMethod.value());
                    if(!baseUrl.equals("/")){
                        url = baseUrl + url;
                    }
                    urlMap.put(url, new Object[]{newClazz, method});
                }
            }
        }
    }

    // 纠正url
    private String correctUrl(String url){

        if(!url.startsWith("/")){
            url = "/" + url;
        }

        if(url.endsWith("/")){
            url = url.substring(0, url.length() - 1);
        }

        return url;
    }

    // 获取url对应的方法
    private Object[] getMethod(String url){
        return urlMap.get(url);
    }

    // 执行方法
    public Object invoke(String url, Object... params) throws CarrotException {
        Object[] objects = getMethod(url);
        if (objects == null) {
            log.info(Msg.ERROR_NOT_FIND_URL + url);
            return null;
        }
        Object clazz = objects[0];
        Method method = (Method) objects[1];

        try {
            return method.invoke(clazz, params);
        } catch (Exception e) {
            throw new CarrotException(method.getName()+ " 方法执行异常","500");
        }
    }

    // 加载
    public static UrlDistribute load(List<Class<?>> scannerClass){
        return new UrlDistribute(scannerClass);
    }
}
