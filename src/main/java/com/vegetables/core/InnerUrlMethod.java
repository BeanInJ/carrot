package com.vegetables.core;

import com.vegetables.annotation.Controller;
import com.vegetables.system.dict.Msg;
import com.vegetables.entity.HttpGetter;
import com.vegetables.entity.HttpSetter;
import com.vegetables.system.notch.YouCanChange;

import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;

/**
 * 静态资源类：将路由映射到对应的方法上
 */
public class InnerUrlMethod implements YouCanChange {
    private static final Logger log = Logger.getGlobal();
    private static final Map<String, Object[]> urlMap = new HashMap<>();

    private InnerUrlMethod() {
    }

    // 纠正url
    private static String correctUrl(String url) {

        if (!url.startsWith("/")) {
            url = "/" + url;
        }

        if (url.endsWith("/")) {
            if (url.length() != 1) {
                url = url.substring(0, url.length() - 1);
            }
        }

        return url;
    }

    // 获取url对应的方法
    private static Object[] getMethod(String url) {
        return urlMap.get(url);
    }

    // 执行Controller中对应的方法
    public static Object HttpToController(HttpGetter httpGetter, HttpSetter httpSetter) throws Exception {
        List<Object> params = new LinkedList<>();

        String url = httpGetter.getUrl();
        Object[] objects = getMethod(url);

        // 未注册URL
        if (objects == null) {
            httpSetter.setCode("404");
            log.info(Msg.ERROR_NOT_FIND_URL + url);
            return null;
        }

        Object clazz = objects[0];
        Method method = (Method) objects[1];
        for (Class<?> parameterType : method.getParameterTypes()) {
            // 判断parameterType的类型
            if (parameterType.equals(HttpGetter.class)) {
                // 如果是HttpGetter类型，则将HttpGetter对象放入params中
                params.add(httpGetter);
            } else if (parameterType.equals(HttpSetter.class)) {
                // 如果是HttpSetter类型，则将HttpSetter对象放入params中
                params.add(httpSetter);
            } else {
                // 目前前端传过来的全部是String类型，还未进行类型转化
                params.add(httpGetter.getBody());
            }
        }

        // 将list转化为Object[]
        Object[] param = params.toArray();

        try {
            return method.invoke(clazz, param);
        } catch (Exception e) {
            log.info(clazz.getClass().getName() + "." + method.getName() + " 方法执行异常");
            throw e;
        }
    }

    // 加载
    protected static void load() {
        Set<Class<?>> classes = InnerScanner.getControllers();
        for (Class<?> clazz : classes) {
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
            String baseUrl = correctUrl(controllerInClass.value());

            // 获取方法上的url
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (method.isAnnotationPresent(Controller.class)) {
                    Controller controllerInMethod = method.getAnnotation(Controller.class);
                    String url = correctUrl(controllerInMethod.value());
                    if (!baseUrl.equals("/")) {
                        url = baseUrl + url;
                    }

                    // 已存在并且不能覆盖的url，无法进行注册
                    if(urlMap.containsKey(url) && !controllerInMethod.isCover()) {
                        log.info("url: " + url +
                                Msg.ERROR_URL_DUPLICATE +
                                clazz.getName()+ "." + method.getName());

                        continue;
                    }

                    urlMap.put(url, new Object[]{newClazz, method});

                }
            }
        }

        // 打印urlMap
        urlMapPrint();
    }

    protected static void update(Map<String, Object[]> newUrlMap) {
        urlMap.clear();
        urlMap.putAll(newUrlMap);
    }

    private static void urlMapPrint() {
        urlMap.forEach((k, v) -> {
            String path = v[1].toString();
            String[] s = path.split("\\.");
            String method = s[s.length - 2] + "." + s[s.length - 1];

            log.info("url: " + k + "  映射方法: " + method);
        });
    }
}
