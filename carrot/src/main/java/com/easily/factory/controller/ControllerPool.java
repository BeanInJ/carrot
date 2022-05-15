package com.easily.factory.controller;

import com.easily.core.ConfigCenter;
import com.easily.factory.ClassPool;
import com.easily.label.*;
import com.easily.system.dict.INNER;
import com.easily.system.dict.MSG;
import com.easily.system.util.UrlUtils;
import jdk.nashorn.internal.runtime.regexp.joni.Config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ControllerPool extends ClassPool {
    private static final Logger log = Logger.getGlobal();

    // 产品容器
    private final ControllerContainer container = new ControllerContainer();
    private final ConfigCenter configCenter;

    public ControllerPool(ConfigCenter configCenter){
        this.configCenter =configCenter;
    }

    @Override
    public Class<? extends Annotation> getLabel() {
        return Controller.class;
    }

    @Override
    public String getPoolName() {
        return INNER.CONTROLLER_POOl_NAME;
    }

    @Override
    public void parseToContainer() {
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(AddUrls.class)) {
                urlsToContainer(clazz);
            } else {
                controllerToContainer(clazz);
            }
        }
        container.urlMapPrint();
    }

    /**
     * 获取产品容器
     */
    @Override
    public <T> T getProductContainer(Class<T> clazz) {
        return clazz.isAssignableFrom(ControllerContainer.class) ? clazz.cast(this.container) : null;
    }

    private void urlsToContainer(Class<?> clazz) {
        // 识别urls类
        urlsMethods:
        for (Method method : clazz.getDeclaredMethods()) {
            // 识别urls方法
            if (method.getParameterCount() == 1) {
                Urls urls = new Urls();
                for (Class<?> parameterType : method.getParameterTypes()) {
                    // 判断参数是否是Urls类型
                    if (!parameterType.equals(Urls.class)) continue urlsMethods;
                }

                try {
                    Object o = clazz.newInstance();
                    putFieldsValue(o);
                    Object[] prams = new Object[]{urls};
                    method.invoke(o, prams);
                    Map<String, Object[]> urlsMap = urls.getUrls();
                    Map<String, Object[]> newUrlsMap = new HashMap<>();

                    String urlPrefix = "";
                    String urlSuffix = "";

                    if (method.isAnnotationPresent(Prefix.class)) {
                        Prefix prefix = method.getAnnotation(Prefix.class);
                        urlPrefix = prefix.value();
                        urlPrefix = UrlUtils.correctUri(urlPrefix);
                    }
                    if (method.isAnnotationPresent(Suffix.class)) {
                        Suffix suffix = method.getAnnotation(Suffix.class);
                        urlSuffix = suffix.value();
                    }

                    // 循环invoke
                    for (Map.Entry<String, Object[]> entry : urlsMap.entrySet()) {
                        String url = entry.getKey();
                        newUrlsMap.put(urlPrefix + url + urlSuffix, entry.getValue());
                    }
                    this.container.putAll(newUrlsMap);
                } catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private void controllerToContainer(Class<?> clazz) {
        Controller controllerInClass = clazz.getAnnotation(Controller.class);
        // 创建对象
        Object newClazz;
        try {
            newClazz = clazz.newInstance();
            putFieldsValue(newClazz);
        } catch (Exception e) {
            e.printStackTrace();
            return;
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
                if (container.containsKey(url)) {
                    if (controllerInMethod.isCover()) {
                        // 覆盖
                        Object oldClass = container.getMethod(url)[0];
                        Method oldMethod = (Method) container.getMethod(url)[1];
                        log.fine("url: " + url +
                                MSG.ERROR_URL_DUPLICATE +
                                oldClass.getClass().getName() + "." + oldMethod.getName());
                    } else {
                        // 不能覆盖
                        log.fine("url: " + url +
                                MSG.ERROR_URL_DUPLICATE +
                                clazz.getName() + "." + method.getName());
                        continue;
                    }
                }
                container.put(url, new Object[]{newClazz, method});
            }
        }
    }

    /**
     * 注入值
     */
    private void putFieldsValue(Object obj) throws IllegalAccessException {
        for (Field field : obj.getClass().getDeclaredFields()) {
            if(field.isAnnotationPresent(ConfigValue.class)){
                field.setAccessible(true);
                Class<?> type = field.getType();
                ConfigValue annotation = field.getAnnotation(ConfigValue.class);
                String value = annotation.value();
                field.set(obj, this.configCenter.get(value,type));
            }
        }
    }

}
